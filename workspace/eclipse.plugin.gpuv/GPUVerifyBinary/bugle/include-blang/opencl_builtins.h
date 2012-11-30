#ifndef OPENCL_BUILTINS_H
#define OPENCL_BUILTINS_H

// 6.11.2: Math functions

#define _MATH_UNARY_FUNC_OVERLOAD(NAME, GENTYPE) \
    GENTYPE __##NAME##_##GENTYPE(GENTYPE x); \
    _CLC_INLINE _CLC_OVERLOAD GENTYPE NAME(GENTYPE x) { \
        return __##NAME##_##GENTYPE(x); \
    }

#define _MATH_BINARY_FUNC_OVERLOAD(NAME, GENTYPE) \
    GENTYPE __##NAME##_##GENTYPE(GENTYPE x, GENTYPE y); \
    _CLC_INLINE _CLC_OVERLOAD GENTYPE NAME(GENTYPE x, GENTYPE y) { \
        return __##NAME##_##GENTYPE(x, y); \
    }

#define _MATH_TERNARY_FUNC_OVERLOAD(NAME, GENTYPE) \
    GENTYPE __##NAME##_##GENTYPE(GENTYPE x, GENTYPE y, GENTYPE z); \
    _CLC_INLINE _CLC_OVERLOAD GENTYPE NAME(GENTYPE x, GENTYPE y, GENTYPE z) { \
        return __##NAME##_##GENTYPE(x, y, z); \
    }


#define _FLOAT_UNARY_MACRO(NAME)          \
    _MATH_UNARY_FUNC_OVERLOAD(NAME, float); \
    _MATH_UNARY_FUNC_OVERLOAD(NAME, float2); \
    _MATH_UNARY_FUNC_OVERLOAD(NAME, float3); \
    _MATH_UNARY_FUNC_OVERLOAD(NAME, float4); \
    _MATH_UNARY_FUNC_OVERLOAD(NAME, float8); \
    _MATH_UNARY_FUNC_OVERLOAD(NAME, float16);

#define _FLOAT_BINARY_MACRO(NAME)          \
    _MATH_BINARY_FUNC_OVERLOAD(NAME, float); \
    _MATH_BINARY_FUNC_OVERLOAD(NAME, float2); \
    _MATH_BINARY_FUNC_OVERLOAD(NAME, float3); \
    _MATH_BINARY_FUNC_OVERLOAD(NAME, float4); \
    _MATH_BINARY_FUNC_OVERLOAD(NAME, float8); \
    _MATH_BINARY_FUNC_OVERLOAD(NAME, float16);

#define _FLOAT_TERNARY_MACRO(NAME)          \
    _MATH_TERNARY_FUNC_OVERLOAD(NAME, float); \
    _MATH_TERNARY_FUNC_OVERLOAD(NAME, float2); \
    _MATH_TERNARY_FUNC_OVERLOAD(NAME, float3); \
    _MATH_TERNARY_FUNC_OVERLOAD(NAME, float4); \
    _MATH_TERNARY_FUNC_OVERLOAD(NAME, float8); \
    _MATH_TERNARY_FUNC_OVERLOAD(NAME, float16);

// Table 6.8

#define acos __clc_acos
_FLOAT_UNARY_MACRO(acos);

#define atan2 __clc_atan2
_FLOAT_BINARY_MACRO(atan2);
_FLOAT_UNARY_MACRO(cbrt);

#define floor __clc_floor
_FLOAT_UNARY_MACRO(floor);

#define fmax __clc_fmax
_FLOAT_BINARY_MACRO(fmax);

#define fmin __clc_fmin
_FLOAT_BINARY_MACRO(fmin);

#define _POWN_MACRO(FLOATGENTYPE, INTGENTYPE) \
    FLOATGENTYPE __pown##_##FLOATGENTYPE##_##INTGENTYPE(FLOATGENTYPE x, INTGENTYPE y); \
    _CLC_INLINE _CLC_OVERLOAD FLOATGENTYPE pown(FLOATGENTYPE x, INTGENTYPE y) { \
        return __pown##_##FLOATGENTYPE##_##INTGENTYPE(x, y); \
    }


_POWN_MACRO(float, int);
_POWN_MACRO(float2, int2);
_POWN_MACRO(float3, int3);
_POWN_MACRO(float4, int4);
_POWN_MACRO(float8, int8);
_POWN_MACRO(float16, int16);

#undef _POWN_MACRO



// Table 6.9

_FLOAT_UNARY_MACRO(half_cos)
_FLOAT_UNARY_MACRO(half_divide)
_FLOAT_UNARY_MACRO(half_exp)
_FLOAT_UNARY_MACRO(half_exp2)
_FLOAT_UNARY_MACRO(half_exp10)
_FLOAT_UNARY_MACRO(half_log)
_FLOAT_UNARY_MACRO(half_log2)
_FLOAT_UNARY_MACRO(half_log10)
_FLOAT_BINARY_MACRO(half_powr)
_FLOAT_UNARY_MACRO(half_recip)
_FLOAT_UNARY_MACRO(half_rsqrt)
_FLOAT_UNARY_MACRO(half_sin)
_FLOAT_UNARY_MACRO(half_sqrt)
_FLOAT_UNARY_MACRO(half_tan)




// 6.11.3: Integer functions

#define _INTEGER_UNARY_MACRO(NAME)          \
    _MATH_UNARY_FUNC_OVERLOAD(NAME, char); \
    _MATH_UNARY_FUNC_OVERLOAD(NAME, char2); \
    _MATH_UNARY_FUNC_OVERLOAD(NAME, char3); \
    _MATH_UNARY_FUNC_OVERLOAD(NAME, char4); \
    _MATH_UNARY_FUNC_OVERLOAD(NAME, char8); \
    _MATH_UNARY_FUNC_OVERLOAD(NAME, char16); \
    _MATH_UNARY_FUNC_OVERLOAD(NAME, uchar); \
    _MATH_UNARY_FUNC_OVERLOAD(NAME, uchar2); \
    _MATH_UNARY_FUNC_OVERLOAD(NAME, uchar3); \
    _MATH_UNARY_FUNC_OVERLOAD(NAME, uchar4); \
    _MATH_UNARY_FUNC_OVERLOAD(NAME, uchar8); \
    _MATH_UNARY_FUNC_OVERLOAD(NAME, uchar16); \
    _MATH_UNARY_FUNC_OVERLOAD(NAME, short); \
    _MATH_UNARY_FUNC_OVERLOAD(NAME, short2); \
    _MATH_UNARY_FUNC_OVERLOAD(NAME, short3); \
    _MATH_UNARY_FUNC_OVERLOAD(NAME, short4); \
    _MATH_UNARY_FUNC_OVERLOAD(NAME, short8); \
    _MATH_UNARY_FUNC_OVERLOAD(NAME, short16); \
    _MATH_UNARY_FUNC_OVERLOAD(NAME, ushort); \
    _MATH_UNARY_FUNC_OVERLOAD(NAME, ushort2); \
    _MATH_UNARY_FUNC_OVERLOAD(NAME, ushort3); \
    _MATH_UNARY_FUNC_OVERLOAD(NAME, ushort4); \
    _MATH_UNARY_FUNC_OVERLOAD(NAME, ushort8); \
    _MATH_UNARY_FUNC_OVERLOAD(NAME, ushort16); \
    _MATH_UNARY_FUNC_OVERLOAD(NAME, int); \
    _MATH_UNARY_FUNC_OVERLOAD(NAME, int2); \
    _MATH_UNARY_FUNC_OVERLOAD(NAME, int3); \
    _MATH_UNARY_FUNC_OVERLOAD(NAME, int4); \
    _MATH_UNARY_FUNC_OVERLOAD(NAME, int8); \
    _MATH_UNARY_FUNC_OVERLOAD(NAME, int16); \
    _MATH_UNARY_FUNC_OVERLOAD(NAME, uint); \
    _MATH_UNARY_FUNC_OVERLOAD(NAME, uint2); \
    _MATH_UNARY_FUNC_OVERLOAD(NAME, uint3); \
    _MATH_UNARY_FUNC_OVERLOAD(NAME, uint4); \
    _MATH_UNARY_FUNC_OVERLOAD(NAME, uint8); \
    _MATH_UNARY_FUNC_OVERLOAD(NAME, uint16); \
    _MATH_UNARY_FUNC_OVERLOAD(NAME, long); \
    _MATH_UNARY_FUNC_OVERLOAD(NAME, long2); \
    _MATH_UNARY_FUNC_OVERLOAD(NAME, long3); \
    _MATH_UNARY_FUNC_OVERLOAD(NAME, long4); \
    _MATH_UNARY_FUNC_OVERLOAD(NAME, long8); \
    _MATH_UNARY_FUNC_OVERLOAD(NAME, long16); \
    _MATH_UNARY_FUNC_OVERLOAD(NAME, ulong); \
    _MATH_UNARY_FUNC_OVERLOAD(NAME, ulong2); \
    _MATH_UNARY_FUNC_OVERLOAD(NAME, ulong3); \
    _MATH_UNARY_FUNC_OVERLOAD(NAME, ulong4); \
    _MATH_UNARY_FUNC_OVERLOAD(NAME, ulong8); \
    _MATH_UNARY_FUNC_OVERLOAD(NAME, ulong16);

#define _INTEGER_BINARY_MACRO(NAME)          \
    _MATH_BINARY_FUNC_OVERLOAD(NAME, char); \
    _MATH_BINARY_FUNC_OVERLOAD(NAME, char2); \
    _MATH_BINARY_FUNC_OVERLOAD(NAME, char3); \
    _MATH_BINARY_FUNC_OVERLOAD(NAME, char4); \
    _MATH_BINARY_FUNC_OVERLOAD(NAME, char8); \
    _MATH_BINARY_FUNC_OVERLOAD(NAME, char16); \
    _MATH_BINARY_FUNC_OVERLOAD(NAME, uchar); \
    _MATH_BINARY_FUNC_OVERLOAD(NAME, uchar2); \
    _MATH_BINARY_FUNC_OVERLOAD(NAME, uchar3); \
    _MATH_BINARY_FUNC_OVERLOAD(NAME, uchar4); \
    _MATH_BINARY_FUNC_OVERLOAD(NAME, uchar8); \
    _MATH_BINARY_FUNC_OVERLOAD(NAME, uchar16); \
    _MATH_BINARY_FUNC_OVERLOAD(NAME, short); \
    _MATH_BINARY_FUNC_OVERLOAD(NAME, short2); \
    _MATH_BINARY_FUNC_OVERLOAD(NAME, short3); \
    _MATH_BINARY_FUNC_OVERLOAD(NAME, short4); \
    _MATH_BINARY_FUNC_OVERLOAD(NAME, short8); \
    _MATH_BINARY_FUNC_OVERLOAD(NAME, short16); \
    _MATH_BINARY_FUNC_OVERLOAD(NAME, ushort); \
    _MATH_BINARY_FUNC_OVERLOAD(NAME, ushort2); \
    _MATH_BINARY_FUNC_OVERLOAD(NAME, ushort3); \
    _MATH_BINARY_FUNC_OVERLOAD(NAME, ushort4); \
    _MATH_BINARY_FUNC_OVERLOAD(NAME, ushort8); \
    _MATH_BINARY_FUNC_OVERLOAD(NAME, ushort16); \
    _MATH_BINARY_FUNC_OVERLOAD(NAME, int); \
    _MATH_BINARY_FUNC_OVERLOAD(NAME, int2); \
    _MATH_BINARY_FUNC_OVERLOAD(NAME, int3); \
    _MATH_BINARY_FUNC_OVERLOAD(NAME, int4); \
    _MATH_BINARY_FUNC_OVERLOAD(NAME, int8); \
    _MATH_BINARY_FUNC_OVERLOAD(NAME, int16); \
    _MATH_BINARY_FUNC_OVERLOAD(NAME, uint); \
    _MATH_BINARY_FUNC_OVERLOAD(NAME, uint2); \
    _MATH_BINARY_FUNC_OVERLOAD(NAME, uint3); \
    _MATH_BINARY_FUNC_OVERLOAD(NAME, uint4); \
    _MATH_BINARY_FUNC_OVERLOAD(NAME, uint8); \
    _MATH_BINARY_FUNC_OVERLOAD(NAME, uint16); \
    _MATH_BINARY_FUNC_OVERLOAD(NAME, long); \
    _MATH_BINARY_FUNC_OVERLOAD(NAME, long2); \
    _MATH_BINARY_FUNC_OVERLOAD(NAME, long3); \
    _MATH_BINARY_FUNC_OVERLOAD(NAME, long4); \
    _MATH_BINARY_FUNC_OVERLOAD(NAME, long8); \
    _MATH_BINARY_FUNC_OVERLOAD(NAME, long16); \
    _MATH_BINARY_FUNC_OVERLOAD(NAME, ulong); \
    _MATH_BINARY_FUNC_OVERLOAD(NAME, ulong2); \
    _MATH_BINARY_FUNC_OVERLOAD(NAME, ulong3); \
    _MATH_BINARY_FUNC_OVERLOAD(NAME, ulong4); \
    _MATH_BINARY_FUNC_OVERLOAD(NAME, ulong8); \
    _MATH_BINARY_FUNC_OVERLOAD(NAME, ulong16);

#define _INTEGER_TERNARY_MACRO(NAME)          \
    _MATH_TERNARY_FUNC_OVERLOAD(NAME, char); \
    _MATH_TERNARY_FUNC_OVERLOAD(NAME, char2); \
    _MATH_TERNARY_FUNC_OVERLOAD(NAME, char3); \
    _MATH_TERNARY_FUNC_OVERLOAD(NAME, char4); \
    _MATH_TERNARY_FUNC_OVERLOAD(NAME, char8); \
    _MATH_TERNARY_FUNC_OVERLOAD(NAME, char16); \
    _MATH_TERNARY_FUNC_OVERLOAD(NAME, uchar); \
    _MATH_TERNARY_FUNC_OVERLOAD(NAME, uchar2); \
    _MATH_TERNARY_FUNC_OVERLOAD(NAME, uchar3); \
    _MATH_TERNARY_FUNC_OVERLOAD(NAME, uchar4); \
    _MATH_TERNARY_FUNC_OVERLOAD(NAME, uchar8); \
    _MATH_TERNARY_FUNC_OVERLOAD(NAME, uchar16); \
    _MATH_TERNARY_FUNC_OVERLOAD(NAME, short); \
    _MATH_TERNARY_FUNC_OVERLOAD(NAME, short2); \
    _MATH_TERNARY_FUNC_OVERLOAD(NAME, short3); \
    _MATH_TERNARY_FUNC_OVERLOAD(NAME, short4); \
    _MATH_TERNARY_FUNC_OVERLOAD(NAME, short8); \
    _MATH_TERNARY_FUNC_OVERLOAD(NAME, short16); \
    _MATH_TERNARY_FUNC_OVERLOAD(NAME, ushort); \
    _MATH_TERNARY_FUNC_OVERLOAD(NAME, ushort2); \
    _MATH_TERNARY_FUNC_OVERLOAD(NAME, ushort3); \
    _MATH_TERNARY_FUNC_OVERLOAD(NAME, ushort4); \
    _MATH_TERNARY_FUNC_OVERLOAD(NAME, ushort8); \
    _MATH_TERNARY_FUNC_OVERLOAD(NAME, ushort16); \
    _MATH_TERNARY_FUNC_OVERLOAD(NAME, int); \
    _MATH_TERNARY_FUNC_OVERLOAD(NAME, int2); \
    _MATH_TERNARY_FUNC_OVERLOAD(NAME, int3); \
    _MATH_TERNARY_FUNC_OVERLOAD(NAME, int4); \
    _MATH_TERNARY_FUNC_OVERLOAD(NAME, int8); \
    _MATH_TERNARY_FUNC_OVERLOAD(NAME, int16); \
    _MATH_TERNARY_FUNC_OVERLOAD(NAME, uint); \
    _MATH_TERNARY_FUNC_OVERLOAD(NAME, uint2); \
    _MATH_TERNARY_FUNC_OVERLOAD(NAME, uint3); \
    _MATH_TERNARY_FUNC_OVERLOAD(NAME, uint4); \
    _MATH_TERNARY_FUNC_OVERLOAD(NAME, uint8); \
    _MATH_TERNARY_FUNC_OVERLOAD(NAME, uint16); \
    _MATH_TERNARY_FUNC_OVERLOAD(NAME, long); \
    _MATH_TERNARY_FUNC_OVERLOAD(NAME, long2); \
    _MATH_TERNARY_FUNC_OVERLOAD(NAME, long3); \
    _MATH_TERNARY_FUNC_OVERLOAD(NAME, long4); \
    _MATH_TERNARY_FUNC_OVERLOAD(NAME, long8); \
    _MATH_TERNARY_FUNC_OVERLOAD(NAME, long16); \
    _MATH_TERNARY_FUNC_OVERLOAD(NAME, ulong); \
    _MATH_TERNARY_FUNC_OVERLOAD(NAME, ulong2); \
    _MATH_TERNARY_FUNC_OVERLOAD(NAME, ulong3); \
    _MATH_TERNARY_FUNC_OVERLOAD(NAME, ulong4); \
    _MATH_TERNARY_FUNC_OVERLOAD(NAME, ulong8); \
    _MATH_TERNARY_FUNC_OVERLOAD(NAME, ulong16);

_INTEGER_TERNARY_MACRO(clamp);

// 6.11.4: Common functions


_FLOAT_TERNARY_MACRO(clamp)

// 6.11.5: Geometric functions

_CLC_INLINE _CLC_OVERLOAD float fast_length(float p) {
    return half_sqrt(p*p);
}
_CLC_INLINE _CLC_OVERLOAD float fast_length(float2 p) {
    return half_sqrt(p.x*p.x + p.y*p.y);
}
_CLC_INLINE _CLC_OVERLOAD float fast_length(float3 p) {
    return half_sqrt(p.x*p.x + p.y*p.y + p.z*p.z);
}
_CLC_INLINE _CLC_OVERLOAD float fast_length(float4 p) {
    return half_sqrt(p.x*p.x + p.y*p.y + p.z*p.z + p.w*p.w);
}



_CLC_INLINE _CLC_OVERLOAD float fast_normalize(float p) {
    return p*half_rsqrt(p*p);
}
_CLC_INLINE _CLC_OVERLOAD float2 fast_normalize(float2 p) {
    return p*half_rsqrt(p.x*p.x + p.y*p.y);
}
_CLC_INLINE _CLC_OVERLOAD float3 fast_normalize(float3 p) {
    return p*half_rsqrt(p.x*p.x + p.y*p.y + p.z*p.z);
}
_CLC_INLINE _CLC_OVERLOAD float4 fast_normalize(float4 p) {
    return p*half_rsqrt(p.x*p.x + p.y*p.y + p.z*p.z + p.w*p.w);
}







// 6.11.2.1 Floating point macros and pragmas

#define M_E_F                   2.7182818284590452354f  /* e */
#define M_LOG2E_F               1.4426950408889634074f  /* log_2 e */
#define M_LOG10E_F              0.43429448190325182765f /* log_10 e */
#define M_LN2_F                 0.69314718055994530942f /* log_e 2 */
#define M_LN10_F                2.30258509299404568402f /* log_e 10 */
#define M_PI_F                  3.14159265358979323846f /* pi */
#define M_PI_2_F                1.57079632679489661923f /* pi/2 */
#define M_PI_4_F                0.78539816339744830962f /* pi/4 */
#define M_1_PI_F                0.31830988618379067154f /* 1/pi */
#define M_2_PI_F                0.63661977236758134308f /* 2/pi */
#define M_2_SQRTPI_F    1.12837916709551257390f /* 2/sqrt(pi) */
#define M_SQRT2_F               1.41421356237309504880f /* sqrt(2) */
#define M_SQRT1_2_F             0.70710678118654752440f /* 1/sqrt(2) */



// 9.3.6 Vector data load and store functions

_CLC_INLINE float3 vload3(size_t offset, const __global float *p) {
    float3 result;
    result.x = p[3*offset];
    result.y = p[3*offset + 1];
    result.z = p[3*offset + 2];
    return result;
}

#endif

