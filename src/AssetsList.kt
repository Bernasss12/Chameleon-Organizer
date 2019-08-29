import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.zip.ZipEntry
import java.util.zip.ZipFile

class AssetsList() {
    var modid = ""
    var version = ""
    var filename = ""
    val dirs = mutableListOf<String>()
    val files = mutableListOf<String>()
    val todoFiles = mutableListOf<String>()
    val doneFiles = mutableListOf<String>()
    val spareFiles = mutableListOf<String>()
    private lateinit var zip : ZipFile

    fun assignZip(zipFile: ZipFile){
        zip = zipFile
        files.clear()
        filename = zip.name.split("/")[zip.name.split("/").lastIndex]
        modid = filename.split(Regex("-"))[0]
        version = filename.removePrefix("$modid-").removeSuffix(".jar")
        zip.stream().map(ZipEntry::getName).forEach { name ->
            when {
                name.isPng() -> files.add(name)
                name.isDir() && name.hasSuffix("assets/") && name.hasMatching("textures")-> dirs.add(name)
            }
        }
    }

    fun getProgress(doneFolderPath: String){
        Files.list(Paths.get("$doneFolderPath/assets/$modid/textures/")).forEach { println(it) }
    }
}

fun String.hasSuffix(suffix: String): Boolean{
    return this.indexOf(suffix) == 0
}

fun String.hasMatching(match: String): Boolean{
    return this.indexOf(match) != -1
}

fun String.isDir(): Boolean {
    return endsWith('/')
}

fun String.isPng(): Boolean {
    return endsWith(".png")
}