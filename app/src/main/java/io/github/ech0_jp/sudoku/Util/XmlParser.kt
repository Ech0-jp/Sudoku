package io.github.ech0_jp.sudoku.Util

import android.util.Log
import java.lang.reflect.Field

/**
 * Code manipulated from: https://github.com/kongsin/KotlinXMLParser/blob/master/src/core/XMLParser.kt
 * Most code is taken from Kongsin's XmlParser, but is manipulated to better suit my application.
 */
class XmlParser{
    companion object {
        fun FromXml(): Any {

            return 0
        }

        fun ToXml(tagName: String, obj: Any): String {
            val builder = StringBuilder()
            builder.append(openTag(tagName))
            if (obj.javaClass.isArray) {
                builder.append("\n")
                (obj as Array<*>)
                        .filterNotNull()
                        .forEach { builder.append(ToXml(it)) }
            } else {
                Log.d("Game", "obj is NOT array")
                builder.append(obj.toString())
            }
            builder.append(closeTag(tagName) + "\n")
            return builder.toString().replace("\n\n", "\n")
        }

        fun ToXml(obj: Any): String {
            val fields = obj.javaClass.declaredFields
            val builder = StringBuilder()

            builder.append(openTag(obj.javaClass.simpleName) + "\n")
            for (f in fields) {
                f.isAccessible = true
                if (isNativeObject(f)) {
                    if (f.type.isArray) {
                        val data = f.get(obj) as Array<*>
                        for (d in data) {
                            builder.append(openTag(f.name))
                            builder.append(d.toString())
                            builder.append(closeTag(f.name) + "\n")
                        }
                    } else {
                        builder.append(openTag(f.name))
                        builder.append(f.get(obj).toString())
                        builder.append(closeTag(f.name) + "\n")
                    }
                } else {
                    if (f.type.isArray) {
                        val data = f.get(obj) as Array<*>
                        for (d in data) {
                            builder.append(ToXml(obj))
                        }
                    } else {
                        val tmpObj = f.get(obj)
                        if (tmpObj != null)
                            builder.append(ToXml(tmpObj))
                    }
                }
            }
            builder.append(closeTag(obj.javaClass.simpleName) + "\n")
            return builder.toString().replace("\n\n", "\n")
        }

        private fun isNativeObject(field: Field): Boolean {
            val type = field.type
            when (type) {
                Char :: class.java -> return true
                Int :: class.java -> return true
                Short :: class.java -> return true
                Long :: class.java -> return true
                Float :: class.java -> return true
                Double :: class.java -> return true
                Boolean :: class.java -> return true
                else -> return String :: class.java == type
            }
        }

        private fun openTag(tagName: String): String {
            return "<$tagName>"
        }

        private fun closeTag(tagName: String): String {
            return "</$tagName>"
        }
    }
}