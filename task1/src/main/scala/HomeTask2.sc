//Реалзуйте IntArrayBuffer с интерфейсом IntTraversable
trait IntTraversable {
    def isEmpty: Boolean

    def size: Int

    def contains(element: Int): Boolean

    def head: Int

    def tail: IntTraversable

    def ++(traversable: IntTraversable): IntTraversable

    def filter(predicate: Int => Boolean): IntTraversable

    def map(function: Int => Int): IntTraversable

    def flatMap(function: Int => IntTraversable): IntTraversable

    def foreach(function: Int => Unit): Unit
}

class IntArrayBuffer extends IntTraversable {

    private var dataSize = 0
    private var buffer = new Array[Int](16)

    private def throwIfBadIndex(index: Int): Unit = {
        if (index >= dataSize) {
            throw new IndexOutOfBoundsException
        }
    }

    private def data() = {
        buffer.take(dataSize)
    }

    def this(data: Array[Int]) = {
        this()
        buffer = data.clone()
        dataSize = data.length
    }

    def apply(index: Int): Int = {
        throwIfBadIndex(index)
        buffer(index)
    }

    def update(index: Int, element: Int): Unit = {
        throwIfBadIndex(index)
        buffer(index) = element
    }

    def clear(): Unit = {
        dataSize = 0
        buffer = new Array[Int](16)
    }

    def +=(element: Int): IntArrayBuffer = {
        if (dataSize == buffer.length) {
            ensureSize(2 * dataSize + 1)
        }
        buffer(dataSize) = element
        dataSize = dataSize + 1
        this
    }

    def ++=(elements: IntTraversable): IntArrayBuffer = {
        elements.foreach(this += _)
        this
    }

    def remove(index: Int): Int = {
        throwIfBadIndex(index)
        val result = buffer(index)
        buffer = buffer.take(index) ++ buffer.drop(index + 1)
        dataSize = dataSize - 1
        result
    }

    override def isEmpty: Boolean = dataSize == 0

    override def size: Int = dataSize

    override def contains(element: Int): Boolean = data().contains(element)

    override def head: Int = buffer.head

    override def tail: IntArrayBuffer = new IntArrayBuffer(data().drop(1))

    override def ++(traversable: IntTraversable): IntArrayBuffer = new IntArrayBuffer(buffer) ++= traversable

    protected def ensureSize(size: Int): Unit = {
        if (size > buffer.length) {
            val array: Array[Int] = new Array[Int](size)
            Array.copy(buffer, 0, array, 0, dataSize)
            buffer = array
        }
    }

    override def filter(predicate: (Int) => Boolean): IntTraversable = {
        new IntArrayBuffer(data().filter(predicate))
    }

    override def map(function: (Int) => Int): IntTraversable = {
        new IntArrayBuffer(data().map(function))
    }

    override def flatMap(function: (Int) => IntTraversable): IntTraversable = {
        val array = new IntArrayBuffer
        this.foreach(array ++= function(_))
        array
    }

    override def foreach(function: (Int) => Unit): Unit = {
        data().foreach(function)
    }
}

object IntArrayBuffer {
    def empty: IntArrayBuffer = new IntArrayBuffer()

    def apply(elements: Int*): IntArrayBuffer = new IntArrayBuffer(elements.toArray)

    def unapplySeq(buffer: IntArrayBuffer): Option[IntArrayBuffer] = {
        if (buffer.isEmpty) {
            None
        } else {
            Some(buffer)
        }
    }
}

// tests

def testEmpty(): Unit = {
    val buffer: IntArrayBuffer = IntArrayBuffer.empty
    assert(buffer.isEmpty)
    assert(buffer.size == 0)
    println("passed empty")
}

def testSize(): Unit = {
    val buffer: IntArrayBuffer = IntArrayBuffer.empty
    val list = List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

    list.foreach(buffer += _)
    assert(buffer.size == list.size)
    println("passed size")
}

def testHead(): Unit = {
    val buffer: IntArrayBuffer = IntArrayBuffer.empty
    val list = List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

    list.foreach(buffer += _)
    assert(buffer.head == 1)
    println("passed head")
}

def testApply(): Unit = {
    val buffer: IntArrayBuffer = IntArrayBuffer.empty
    val list = List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

    list.foreach(buffer += _)
    list.foreach((i: Int) => {
        assert(buffer(i - 1) == i)
    })
    println("passed apply")
}

def testClear(): Unit = {
    val buffer: IntArrayBuffer = IntArrayBuffer.empty
    val list = List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    list.foreach(buffer += _)

    buffer.clear()

    buffer.foreach(_ => assert(false))
    assert(buffer.isEmpty)
    assert(buffer.size == 0)
    println("passed clear")
}

def testContains(): Unit = {
    val buffer: IntArrayBuffer = IntArrayBuffer.empty
    val list = List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    list.foreach(buffer += _)

    assert(buffer.contains(10))
    assert(buffer.contains(4))
    assert(buffer.contains(1))
    assert(!buffer.contains(15))
    assert(!buffer.contains(0))
    assert(!buffer.contains(-10))

    println("passed contains")
}

def testRemove(): Unit = {
    val buffer: IntArrayBuffer = IntArrayBuffer.empty
    val list = List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    list.foreach(buffer += _)

    buffer.remove(4)

//    buffer.foreach(println(_))

    assert(buffer.size == 9)
    assert(buffer(4) == 6)

    buffer.clear()
    buffer += 1
    buffer.remove(0)
    assert(buffer.isEmpty)

    println("passed remove")
}

testEmpty()
testSize()
testHead()
testApply()
testClear()
testContains()
testRemove()