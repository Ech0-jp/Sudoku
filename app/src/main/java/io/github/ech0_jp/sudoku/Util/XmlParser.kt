package io.github.ech0_jp.sudoku.Util

import android.util.Log
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import java.io.ByteArrayInputStream
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import javax.xml.parsers.DocumentBuilderFactory

/**
 * Code manipulated from: https://github.com/kongsin/KotlinXMLParser/blob/master/src/core/XMLParser.kt
 * Most code is taken from Kongsin's XmlParser, but is manipulated to better suit my application.
 */
class XmlParser{
    companion object {
        fun FromXml(xml: String, nodeName: String, type: Any): Any? {
            Log.d("XmlParser", "Loading value of type: $type from node: $nodeName")
            val builder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
            val doc = builder.parse(ByteArrayInputStream(xml.toByteArray()))
            doc.documentElement.normalize()
            val element = getData(doc.documentElement.getElementsByTagName(nodeName))
            if (element.first() == null) {
                Log.d("XmlParser", "Could not find element by name of '$nodeName'")
                return null
            }
            if (isNativeObject(type)){
                return getValue(type, element.first()!!)
            } else {
                return getNodeObject(element.first()!!, type.javaClass)
            }
        }

        fun <T>FromXml(xml: String, nodeName: String, arrayType: Class<T>): Array<T>? {
            Log.d("XmlParser", "Loading Array<${arrayType.simpleName}> from Xml under nodeName:$nodeName")
            val builder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
            val doc = builder.parse(ByteArrayInputStream(xml.toByteArray()))
            doc.documentElement.normalize()
            val element = getData(doc.documentElement.getElementsByTagName(nodeName))
            if (element.first() == null) {
                Log.d("XmlParser", "Could not find element by name of '$nodeName'")
                return null
            }
            return getNodeArray(element.first()!!, arrayType)
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
                if (Modifier.isFinal(f.modifiers) || f.type.isInterface) continue
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

        private fun isNativeObject(obj: Any): Boolean {
            if (obj is Char) return true
            else if (obj is Int) return true
            else if (obj is Short) return true
            else if (obj is Long) return true
            else if (obj is Float) return true
            else if (obj is Double) return true
            else if (obj is Boolean) return true
            else return obj is String
        }

        @Suppress("UNCHECKED_CAST")
        private  fun <T>getNodeObject(element: Element, obj: Class<T>): T{
            Log.d("XmlParser", "getNodeObject(${element.tagName}, $obj)")
            val _obj = obj.newInstance()
            val fields = obj.declaredFields
            for (f in fields) {
                if (Modifier.isFinal(f.modifiers) || f.type.isInterface) continue
                f.isAccessible = true
                if (isNativeObject(f)) {
                    putValue(f, _obj, element)
                } else {
                    if (f.type.isArray) {
                        val elm = getData(element.getElementsByTagName(f.name))
                        elm.size.let {
                            val tmpObj = java.lang.reflect.Array.newInstance(f.type.componentType, elm.size) as Array<Any>
                            for (i in 0..tmpObj.size - 1) {
                                tmpObj[i] = getNodeObject(elm[i] as Element, f.type.componentType)
                            }
                            f.set(_obj, tmpObj)
                        }
                    } else {
                        val tmpObj = getNodeObject(element, f.type)
                        f.set(_obj, tmpObj)
                    }
                }
            }
            return _obj
        }

        @Suppress("UNCHECKED_CAST")
        private fun <T>getNodeArray(element: Element, arrayType: Class<T>): Array<T> {
            Log.d("XmlParser", "getNodeArray() arrayType.simpleName:${arrayType.simpleName}")
            val elements = getData(element.getElementsByTagName(arrayType.simpleName))
            Log.d("XmlParser", "getNodeArray() getting child elements of type ${arrayType.simpleName}.. elements.size:${elements.size}")
            val array = java.lang.reflect.Array.newInstance(arrayType, elements.size) as Array<T>
            for (i in 0..elements.size - 1) {
                if (elements[i] == null) continue
                val tmpObj = getNodeObject(elements[i]!!, arrayType)
                array[i] = tmpObj
            }
            return array
        }

        private fun getData(list: NodeList): Array<Element?> {
            val e: Array<Element?> = arrayOfNulls(list.length)
            e.let {
                for (i in 0..list.length - 1) {
                    e[i] = list.item(i) as Element?
                }
            }
            return e
        }

        private fun <T>putValue(f: Field, obj: T, value: Element): T {
            val type = f.type
            val v = getData(value.getElementsByTagName(f.name))
            when (type) {
                Char::class.java ->
                        if (f.type.isArray) {
                            val ch = arrayOfNulls<Char?>(v.size)
                            for (i in 0..ch.size - 1) {
                                ch[i] = v[i]!!.textContent.toCharArray()[i]
                            }
                            f.set(obj, ch)
                        } else {
                            for (e in v) {
                                f.setChar(obj, e!!.textContent[0])
                            }
                        }
                Int :: class.java ->
                    if (f.type.isArray) {
                        val ch = arrayOfNulls<Int?>(v.size)
                        for (i in 0..ch.size - 1) {
                            ch[i] = v[i]!!.textContent.toInt()
                        }
                        f.set(obj, ch)
                    } else {
                        for (e in v) {
                            f.setInt(obj, e!!.textContent.trim().toInt())
                        }
                    }
                Short :: class.java ->
                    if (f.type.isArray) {
                        val ch = arrayOfNulls<Short?>(v.size)
                        for (i in 0..ch.size - 1) {
                            ch[i] = v[i]!!.textContent.toShort()
                        }
                        f.set(obj, ch)
                    } else {
                        for (e in v) {
                            f.setShort(obj, e!!.textContent.toShort())
                        }
                    }
                Long :: class.java ->
                    if (f.type.isArray) {
                        val ch = arrayOfNulls<Long?>(v.size)
                        for (i in 0..ch.size - 1) {
                            ch[i] = v[i]!!.textContent.toLong()
                        }
                        f.set(obj, ch)
                    } else {
                        for (e in v) {
                            f.setLong(obj, e!!.textContent.toLong())
                        }
                    }
                Boolean :: class.java ->
                    if (f.type.isArray) {
                        val ch = arrayOfNulls<Boolean?>(v.size)
                        for (i in 0..ch.size - 1) {
                            ch[i] = v[i]!!.textContent.toBoolean()
                        }
                        f.set(obj, ch)
                    } else {
                        for (e in v) {
                            f.setBoolean(obj, e!!.textContent.toBoolean())
                        }
                    }
                Float :: class.java ->
                    if (f.type.isArray) {
                        val ch = arrayOfNulls<Float?>(v.size)
                        for (i in 0..ch.size - 1) {
                            ch[i] = v[i]!!.textContent.toFloat()
                        }
                        f.set(obj, ch)
                    } else {
                        for (e in v) {
                            f.setFloat(obj, e!!.textContent.toFloat())
                        }
                    }
                Double :: class.java ->
                    if (f.type.isArray) {
                        val ch = arrayOfNulls<Double?>(v.size)
                        for (i in 0..ch.size - 1) {
                            ch[i] = v[i]!!.textContent.toDouble()
                        }
                        f.set(obj, ch)
                    } else {
                        for (e in v) {
                            f.setDouble(obj, e!!.textContent.toDouble())
                        }
                    }
                String :: class.java ->
                    if (f.type.isArray) {
                        val ch = arrayOfNulls<String?>(v.size)
                        for (i in 0..ch.size - 1) {
                            ch[i] = v[i]!!.textContent
                        }
                        f.set(obj, ch)
                    } else {
                        for (e in v) {
                            f.set(obj, e!!.textContent)
                        }
                    }
            }
            return obj
        }

        private fun getValue(type: Any, value: Element): Any {
            if (type is Char) {
                return value.textContent[0]
            } else if (type is Int) {
                return value.textContent.trim().toInt()
            } else if (type is Short) {
                return value.textContent.toShort()
            } else if (type is Long) {
                return value.textContent.toLong()
            } else if (type is Boolean) {
                return value.textContent.toBoolean()
            } else if (type is Float) {
                return value.textContent.toFloat()
            } else if (type is Double) {
                return value.textContent.toDouble()
            } else { //String
                return value.textContent
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