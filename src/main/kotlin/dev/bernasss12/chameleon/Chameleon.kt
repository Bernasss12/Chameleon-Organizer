package dev.bernasss12.chameleon.kotlin

import java.io.File
import java.util.zip.ZipFile

fun main(args: Array<String>) {
    val configs = ChameleonConfigs(".chameleon.cfg")
    val availibleJars = mutableListOf<String>()
    val doneAssets = mutableListOf<String>()
    fun MutableList<String>.getAvailibleJars() :MutableList<String>{
        File(configs.paths.jars).walk().forEach {
            if (it.toString().endsWith(".jar")) {
                this.add(it.toString())
                printlnGreen("Found: $it")
            }
        }
        return this
    }
    fun MutableList<String>.getDoneAssets() {
        File(configs.paths.done).walk().forEach {
            if (it.toString().endsWith(".png")) {
                this.add(it.toString())
                printlnGreen("Found: $it")
            }
        }
    }
    printlnInfo("Loading configs...")
    configs.loadConfigs()
    printlnInfo("Listing all available jar files...")
    var mods = mutableListOf<ModJar>()
    availibleJars.getAvailibleJars().forEach{
        mods.add(ModJar(ZipFile(it)).load())
    }
    printlnInfo("Loaded all!")
    /*
    try {
        var zip = ZipFile(File("${configs.paths.jars}logisticspipes-0.10.2.203.jar"), ZipFile.OPEN_READ)
        var assetlist = AssetsList()
        assetlist.assignZip(zip)
        assetlist.files.forEach(::println)
    } catch (e: Exception) {
        printlnError(e.toString())
    }*/
}

class ChameleonConfigs(val configFile: String) {
    private val fileConfigs = mutableMapOf<String, String>()
    val paths = FolderPaths()

    fun loadConfigs() {
        try {
            File(configFile).forEachLine {
                if (it.isNotBlank() && it.indexOf('=') != -1) {
                    val its = it.split("=")
                    fileConfigs[its[0]] = its[1]
                }
            }
            paths.jars = fileConfigs["jars"].toString()
            paths.done = fileConfigs["done"].toString()
            paths.todo = fileConfigs["todo"].toString()
        } catch (e: Exception) {
            printlnError("Failed to load configs.")
        }
        paths.getPaths()
        saveConfigs()
    }

    fun saveConfigs() {
        var string = ""
        string += "jars=${paths.jars}\n"
        string += "done=${paths.done}\n"
        string += "todo=${paths.todo}\n"
        File(configFile).bufferedWriter().use { out ->
            out.write(string)
            printlnInfo("Saved config.")
        }
    }
}

enum class MenuStates {
    Loading,
    MainMenu,
    DirsMenu,
    JarsMenu,
}


