import java.io.File
import java.util.zip.ZipFile

fun main(args: Array<String>) {
    try {
        var zip = ZipFile(File("jars/logisticspipes-0.10.2.203.jar"), ZipFile.OPEN_READ)
        var assetlist = AssetsList()
        assetlist.assignZip(zip)
        assetlist.files.forEach(::println)
        assetlist.getProgress("done/")
    } catch (e: Exception) {
        print(e)
    }
}

