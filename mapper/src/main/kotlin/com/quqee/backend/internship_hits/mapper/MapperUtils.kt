package com.quqee.backend.internship_hits.mapper

import kotlin.reflect.KClass

fun <ApiClass : Any, InternalClass : Any> makeFromApiMapper(
    apiType: KClass<ApiClass>,
    internalType: KClass<InternalClass>,
    transform: (ApiClass) -> InternalClass
) = object : FromApiToInternalMapper<ApiClass, InternalClass> {
    override fun fromApi(item: ApiClass): InternalClass = transform(item)

    override fun apiType(): KClass<ApiClass> = apiType

    override fun internalType(): KClass<InternalClass> = internalType
}

inline fun <reified ApiClass : Any, reified InternalClass : Any> makeFromApiMapper(
    noinline transform: (ApiClass) -> InternalClass
) = makeFromApiMapper(
    apiType = ApiClass::class,
    internalType = InternalClass::class,
    transform = transform
)

fun <ApiClass : Any, InternalClass : Any> makeToApiMapper(
    apiType: KClass<ApiClass>,
    internalType: KClass<InternalClass>,
    transform: (InternalClass) -> ApiClass
) = object : FromInternalToApiMapper<ApiClass, InternalClass> {
    override fun fromInternal(item: InternalClass): ApiClass = transform(item)

    override fun apiType(): KClass<ApiClass> = apiType

    override fun internalType(): KClass<InternalClass> = internalType
}

inline fun <reified ApiClass : Any, reified InternalClass : Any> makeToApiMapper(
    noinline transform: (InternalClass) -> ApiClass
) = makeToApiMapper(
    apiType = ApiClass::class,
    internalType = InternalClass::class,
    transform = transform
)

fun <ApiType : Enum<ApiType>, InternalType : Enum<InternalType>> makeEnumerationMapper(
    apiType: KClass<ApiType>,
    internalType: KClass<InternalType>
): EnumerationMapper<ApiType, InternalType> {
    return object : EnumerationMapper<ApiType, InternalType> {
        override fun mapToApi(internal: InternalType): ApiType {
            return java.lang.Enum.valueOf(apiType.java, internal.name)
        }

        override fun mapToInternal(api: ApiType): InternalType {
            return java.lang.Enum.valueOf(internalType.java, api.name)
        }

        override fun apiType(): KClass<ApiType> {
            return apiType
        }

        override fun internalType(): KClass<InternalType> {
            return internalType
        }
    }
}