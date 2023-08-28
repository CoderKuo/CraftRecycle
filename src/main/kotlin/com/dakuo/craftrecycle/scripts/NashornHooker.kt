package com.dakuo.craftrecycle.scripts

import java.io.Reader
import javax.script.Compilable
import javax.script.CompiledScript
import javax.script.ScriptEngine

abstract class NashornHooker {

    abstract fun getNashornEngine():ScriptEngine


    abstract fun getGlobalEngine(): ScriptEngine

    abstract fun getNashornEngine(args: Array<String>): ScriptEngine

    abstract fun getNashornEngine(args: Array<String>, classLoader: ClassLoader): ScriptEngine

    abstract fun compile(string: String): CompiledScript

    abstract fun compile(reader: Reader): CompiledScript

    fun compile(engine: ScriptEngine, string: String): CompiledScript {
        return (engine as Compilable).compile(string)
    }

    fun compile(engine: ScriptEngine, reader: Reader): CompiledScript {
        return (engine as Compilable).compile(reader)
    }

    abstract fun invoke(compiledScript: com.dakuo.craftrecycle.scripts.CompiledScript, function: String, map: Map<String, Any>?, vararg args: Any): Any?

    abstract fun isFunction(engine: ScriptEngine, func: Any?): Boolean

    fun isFunction(engine: ScriptEngine, func: String): Boolean {
        return isFunction(engine, engine.get(func))
    }
}