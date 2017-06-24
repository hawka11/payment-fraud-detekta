package detekta.payment.map

import co.paralleluniverse.galaxy.Messenger
import co.paralleluniverse.galaxy.Store
import co.paralleluniverse.strands.Strand
import detekta.payment.cypher.objectMapper
import org.slf4j.LoggerFactory

data class TreeNode<V>(

        var key: String,
        var value: V?,

        var children: LongArray,
        var keys: Array<String?>,
        var numChildren: Int,

        var parent: Long,
        var isLeaf: Boolean
) {

    fun <V> insertLeafNode(id: Long, c: TreeNode<V>, idx: Int): TreeNode<V> {
        this.children[idx] = id
        this.keys[idx] = c.key
        this.numChildren++

        return c
    }

    companion object TreeNodeC {
        fun <V> create(bf: Int, key: String, parent: Long) =
                TreeNode<V>(key, null, LongArray(bf), arrayOfNulls<String>(bf + 1), 0, parent, false)

        fun <V> createLeaf(bf: Int, key: String, value: V, parent: Long) =
                TreeNode(key, value, LongArray(bf), arrayOfNulls<String>(bf + 1), 0, parent, true)
    }
}

class TreeMap<V>(val store: Store, val messenger: Messenger, val treeName: String) {

    private val LOG = LoggerFactory.getLogger(TreeMap::class.java)
    private val ROOT_PARENT = Long.MAX_VALUE

    private val bf = 3
    private val objectMapper = objectMapper()

    private val root: Long by lazy {
        val txn = store.beginTransaction()

        val rootId = store.getRoot(treeName, txn)

        if (store.isRootCreated(rootId, txn)) {
            LOG.info("initializing tree: " + Strand.currentStrand().id)
            val newRoot = TreeNode.create<V>(bf, "", ROOT_PARENT)
            store.set(rootId, writeNode(newRoot), txn)
        }

        store.commit(txn)

        rootId
    }

    fun put(k: String, v: V) {
        val r = extractNode(root)
        val txn = store.beginTransaction()

        val newLeaf = TreeNode.createLeaf(bf, k, v, root)
        val newLeafId = store.put(writeNode(newLeaf), txn)
        if (r.numChildren == 0) {
            r.insertLeafNode(newLeafId, newLeaf, 0)
        } else {
            r.insertLeafNode(newLeafId, newLeaf, 1)
        }

        store.set(root, writeNode(r), txn)

        store.commit(txn)
    }

    fun get(k: String): V? {
        val r = extractNode(root)
        println("num children: " + r.numChildren)
        return search(k, r)
    }

    private fun search(k: String, r: TreeNode<V>): V? {
        for (i in 0..(r.numChildren - 1)) {

            val cmp = k.compareTo(r.keys[i]!!)

            if (cmp == 0) {
                return extractNode(r.children[i]).value
            } else if (cmp < 0) {
                return search(k, extractNode(r.children[i]))
            }
        }
        return null
    }

    private fun writeNode(newRoot: TreeNode<V>) = objectMapper.writeValueAsBytes(newRoot)

    private fun extractNode(id: Long): TreeNode<V> =
            objectMapper.readValue(store.get(id), TreeNode::class.java) as TreeNode<V>
}