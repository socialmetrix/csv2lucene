def lazyEval(value: => String)(op: String => Integer): Integer = {
  try {
    println("before")
    op(value)
  } finally {
    println("after1")
    value.toString
    println("after2")
  }
}

def generator = {
  println("generating...")
  "actual value"
}

lazyEval(generator) { value => 123 }

def eager(value: String)(op: String => Integer): Integer = {
  try {
    println("before")
    op(value)
  } finally {
    println("after1")
    value.toString
    println("after2")
  }
}

eager(generator) { value => 123 }