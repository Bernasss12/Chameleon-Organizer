package dev.bernasss12.chameleon.kotlin

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*
import kotlinx.serialization.list
import java.util.zip.ZipFile
import kotlin.streams.toList

interface Mod {
    val modInfo: McModInfo
    val modAssets: McModAssets
}

class ModJar(private val zip: ZipFile) {
    val mcModInfos = mutableListOf<McModInfo>()
    /*
     *  Get the mod info:
     *  - First - try to retrieve information from mcmod.info file.
     *    - If mcmod.info file is stored as an array with only one element read it as modList and only store first element.
     *    - If mcmod.info file has multiple mod entries flag it accordingly and create multiple ModAsset files as a result.
     *  - Second - try to retrieve information from META-INF/fml_cache_annotation.json
     *  - At last - default version to 1.0.0, mcversion to 1.0 and set the modid from the file name.
     */
    fun load(): ModJar {
        printlnInfo("Loading ${zip.name}")
        try{
            val modInfo = Json.nonstrict.parseJson(zip.getInputStream(zip.stream().filter { n -> n.name == "mcmod.info" }.toList().first()).use {
                it.bufferedReader().readLines().joinToString("")
            })
            when (modInfo) {
                is JsonArray -> {
                    mcModInfos.addAll(Json.nonstrict.parse(McModInfo.serializer().list, modInfo.toString()))
                }
                is JsonObject -> {
                    (modInfo["modList"] as JsonArray).forEach{
                        mcModInfos.add(Json.nonstrict.parse(McModInfo.serializer(), it.toString()))
                    }
                }
                else -> printlnError("Is it really neither an Array nor an Object?")
            }
        }catch(e: Exception){
            if(e is NoSuchElementException){
                printlnInfo("mcmod.info not found for ${zip.name}, looking for fml_cache_annotation.json.")
                try {
                    var values: AnnotationValues
                    val modInfo = Json.nonstrict.parseJson(zip.getInputStream(zip.stream().filter { n -> n.name == "META-INF/fml_cache_annotation.json" }.toList().first()).use {
                            it.bufferedReader().readLines().joinToString("")
                    })
                    (modInfo as JsonObject).forEach { jsonElement ->
                        val annotations = ((jsonElement as JsonObject)["annotations"] as JsonArray)
                        annotations.forEach {   it as JsonObject
                            if(!it.content.isNullOrEmpty() && it["name"]!!.content.contains("Lnet/minecraftforge/fml/common/Mod")){
                                values = Json.nonstrict.parse(AnnotationValues.serializer(), it.toString())
                            }
                        }
                    }
                    printlnGreen("FML Cache not found")
                }catch (e: Exception){
                    if(e is NoSuchElementException){
                        printlnError("fml_cache_annotation.json not found for ${zip.name}, this was the last resort, please yell at the mod developer.")
                    }
                }
            }else printlnWarning(e.toString())
        }
        printlnInfo("Loaded ${zip.name}")
        println("")
        return this
    }

    // Cast this class to a universal class after loading the assets and saving all to a file.
    /*
    fun toLoadedMod(): MutableList<LoadedMod> {
        return LoadedMod(McModInfo("", "", "", ""), McModAssets(mutableListOf<String>(), mutableListOf<String>(), mutableListOf<String>(), mutableListOf<String>(), mutableListOf<String>()))
    }
    */

    fun saveMod() {}
}

@Serializable
data class LoadedMod(
    override val modInfo: McModInfo,
    override val modAssets: McModAssets
) : Mod

@Serializable
data class McModInfo(
    val modid: String,
    val name: String,
    val version: String,
    val mcversion: String
)

@Serializable
data class AnnotationValues(val modid: String, val version: String, val acceptedMinecraftVersions: String)

@Serializable
data class McModAssets(
    val dirs: MutableList<String>,
    val files: MutableList<String>,
    val todoFiles: MutableList<String>,
    val doneFiles: MutableList<String>,
    val spareFiles: MutableList<String>
)

fun String.hasSuffix(suffix: String): Boolean {
    return this.indexOf(suffix) == 0
}

fun String.hasMatching(match: String): Boolean {
    return this.indexOf(match) != -1
}