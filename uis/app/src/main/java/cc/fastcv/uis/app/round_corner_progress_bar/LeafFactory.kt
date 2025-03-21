package cc.fastcv.uis.app.round_corner_progress_bar

import java.util.*

class LeafFactory {

    companion object {
        private const val MAX_LEAF = 6
        private const val LEAF_FLOAT_TIME = 3000
    }

    val random = Random()

    // 生成一个叶子信息
    private fun generateLeaf(): Leaf {
        val leaf = Leaf()
        val randomType = random.nextInt(3)
        // 随时类型－ 随机振幅
        var type = StartType.MIDDLE
        when (randomType) {
            1 -> type = StartType.LITTLE
            2 -> type = StartType.BIG
            else -> {}
        }
        leaf.type = type
        // 随机起始的旋转角度
        leaf.rotateAngle = random.nextInt(360)
        // 随机旋转方向（顺时针或逆时针）
        leaf.rotateDirection = random.nextInt(2)
        // 为了产生交错的感觉，让开始的时间有一定的随机性
        val mAddTime = random.nextInt(((LEAF_FLOAT_TIME * 1.5).toInt()))
        leaf.startTime = System.currentTimeMillis() + mAddTime
        return leaf
    }

    // 根据最大叶子数产生叶子信息
    fun generateLeafs(): List<Leaf> {
        return generateLeafs(MAX_LEAF)
    }

    // 根据传入的叶子数量产生叶子信息
    private fun generateLeafs(leafSize: Int): List<Leaf> {
        val leafList = LinkedList<Leaf>()
        for (i in 0 until leafSize) {
            leafList.add(generateLeaf())
        }
        return leafList
    }
}