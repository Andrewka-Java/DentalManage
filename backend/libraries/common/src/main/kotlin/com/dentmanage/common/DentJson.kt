package com.dentmanage.common

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.BeanDescription
import com.fasterxml.jackson.databind.DeserializationConfig
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationConfig
import com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
import com.fasterxml.jackson.databind.cfg.MapperConfig
import com.fasterxml.jackson.databind.deser.Deserializers
import com.fasterxml.jackson.databind.introspect.AnnotatedClass
import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.Serializers
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import com.fasterxml.jackson.module.kotlin.KotlinFeature.SingletonSupport
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.http4k.format.ConfigurableJackson
import java.time.Instant
import kotlin.reflect.KClass
import kotlin.reflect.full.allSuperclasses

object DentJson : ConfigurableJackson(
    ObjectMapper()
        .configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
        .registerModule(KotlinModule.Builder().enable(SingletonSupport).build())
//        .registerModule(JavaTimeModule())
        .registerModule(DentModule)
        .disable(WRITE_DATES_AS_TIMESTAMPS)
)

private object DentModule : SimpleModule() {
    private fun readResolve(): Any = DentModule
    override fun setupModule(context: SetupContext) {
        super.setupModule(context)
        context.appendAnnotationIntrospector(SealedClassIntrospector)
        context.addSerializers(DentSerializers)
        context.addDeserializers(DentDeserializers)
    }
}

private object DentSerializers : Serializers.Base() {
    override fun findSerializer(config: SerializationConfig, type: JavaType, beanDesc: BeanDescription?): JsonSerializer<*>? =
        when {
            type.rawClass == Instant::class.java -> ToStringSerializer.instance
            type.isTypeOrSubTypeOf(Value::class.java) -> ToStringSerializer.instance
            else -> null
        }
}

private object DentDeserializers : Deserializers.Base() {
    override fun findBeanDeserializer(type: JavaType, config: DeserializationConfig?, beanDesc: BeanDescription): JsonDeserializer<*>? =
        when {
            type.rawClass == Instant::class.java -> deserializer { p, _ -> Instant.parse(p.text) }
            type.isTypeOrSubTypeOf(Value::class.java) -> deserializeFor(beanDesc, type, String::class, JsonParser::getText)
            else -> null
        }
}

private fun <T> deserializer(
    by: (parser: JsonParser, context: DeserializationContext) -> T
): JsonDeserializer<T> =
    object : JsonDeserializer<T>() {
        override fun deserialize(parser: JsonParser, context: DeserializationContext): T = by(parser, context)
    }

private fun <T : Any> deserializeFor(
    beanDesc: BeanDescription,
    type: JavaType,
    rawType: KClass<T>,
    extractor: (JsonParser) -> T
): JsonDeserializer<Any> {
    val constructor = beanDesc.constructors.find { it.parameterCount == 1 && it.getParameter(0).rawType == rawType.java } 
        ?: error("No constructor for ${beanDesc.beanClass} and ${type.rawClass}")
    return deserializer { parser, _ ->
        constructor.call(arrayOf<Any>(extractor(parser)))
    }
}

object SealedClassIntrospector : NopAnnotationIntrospector() {
    private fun readResolve(): Any = SealedClassIntrospector
    override fun findTypeResolver(
        config: MapperConfig<*>,
        ac: AnnotatedClass,
        baseType: JavaType
    ): TypeResolverBuilder<*>? {
        val kotlinClass = baseType.rawClass.kotlin
        val sealedClass = kotlinClass.eligibleSealedClass
        return if (sealedClass != null) {
            StdTypeResolverBuilder()
//                .init(JsonTypeInfo.Id.MINIMAL_CLASS, )
                .typeProperty("type")
                .inclusion(JsonTypeInfo.As.PROPERTY)
                .also { 
                    if (sealedClass != kotlinClass) it.defaultImpl(kotlinClass.java)
                }
        } else super.findTypeResolver(config, ac, baseType)
    }
}

private val KClass<*>.eligibleSealedClass: KClass<*>?
    get() =
        (takeIf { it.isSealed } ?: allSuperclasses.find { it.isSealed })
            ?.takeIf { it.qualifiedName?.startsWith("com.dentmanage") == true }