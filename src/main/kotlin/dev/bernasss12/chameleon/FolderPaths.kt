package dev.bernasss12.chameleon.kotlin

val DEFAULT_JARS_DIR = "jars/"
val DEFAULT_DONE_DIR = "done/"
val DEFAULT_TODO_DIR = "todo/"

class FolderPaths {
    var jars: String = ""
    var done: String = ""
    var todo: String = ""

    fun getPaths(){
        var count = 0;
        if(jars.isBlank() || jars == "null"){
            count++
            jars = getPath("jars")
            if(jars.isBlank()){
                jars = DEFAULT_JARS_DIR
                printlnWarning("Falling to default.")
            }
            if(jars[jars.lastIndex] != '/') jars += "/"
        } else printlnInfo("jars path loaded from config. ")
        if(done.isBlank() || done == "null"){
            count++
            done = getPath("done")
            if(done.isBlank()){
                done = DEFAULT_DONE_DIR
                printlnWarning("Falling to default.")
            }
            if(done[done.lastIndex] != '/') done += "/"
        } else printlnInfo("done path loaded from config. ")
        if(todo.isBlank() || todo == "null"){
            count++
            todo = getPath("todo")
            if(todo.isBlank()){
                todo = DEFAULT_TODO_DIR
                printlnWarning("Falling to default.")
            }
            if(todo[todo.lastIndex] != '/') todo += "/"
        } else printlnInfo("todo path loaded from config. ")
    }

    private fun getPath(dir: String): String{
        print("Please input the relative path to the $dir folder, leave empty for default:\n> ")
        return readLine().toString()
    }
}