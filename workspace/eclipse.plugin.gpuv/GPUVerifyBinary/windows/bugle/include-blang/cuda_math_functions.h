/* TABLE C-1 */
__device__ float rsqrtf(float x);
__device__ float sqrtf(float x);
__device__ float cbrtf(float x);
__device__ float rcbrtf(float x);
__device__ float hypotf(float x, float y);
__device__ float expf(float x);
__device__ float exp2f(float x);
__device__ float exp10f(float x);
__device__ float expm1f(float x);
__device__ float logf(float x);
__device__ float log2f(float x);
__device__ float log10f(float x);
__device__ float log1pf(float x);
__device__ float sinf(float x);
__device__ float cosf(float x);
__device__ float tanf(float x);
__device__ void  sincosf(float x, float *sptr, float *cptr);
__device__ float sinpif(float x);
__device__ float cospif(float x);
__device__ float asinf(float x);
__device__ float acosf(float x);
__device__ float atanf(float x);
__device__ float atan2f(float y, float x);
__device__ float sinhf(float x);
__device__ float coshf(float x);
__device__ float tanhf(float x);
__device__ float asinhf(float x);
__device__ float acoshf(float x);
__device__ float atanhf(float x);
__device__ float powf(float x, float y);
__device__ float erff(float x);
__device__ float erfcf(float x);
__device__ float erfinvf(float x);
__device__ float erfcinvf(float x);
__device__ float erfcxf(float x);
__device__ float lgammaf(float x);
__device__ float tgammaf(float x);
__device__ float fmaf(float x, float y, float z);
__device__ float frexpf(float x, int *exp);
__device__ float ldexpf(float x, int exp);
__device__ float scalbnf(float x, int n);
__device__ float scalblnf(float x, int l);
__device__ float logbf(float x);
__device__ int   ilogbf(float x);
__device__ float j0f(float x);
__device__ float j1f(float x);
__device__ float jnf(float x);
__device__ float y0f(float x);
__device__ float y1f(float x);
__device__ float ynf(int x, float y);
__device__ float fmodf(float x, float y);
__device__ float remainderf(float x, float y);
__device__ float remquof(float x, float y, int *iptr);
__device__ float modff(float x, float *iptr);
__device__ float fdimf(float x, float y);
__device__ float truncf(float x);
__device__ float roundf(float x);
__device__ float rintf(float x);
__device__ float nearbyintf(float x);
__device__ float ceilf(float x);
__device__ float floorf(float x);
__device__ long int lrintf(float x);
__device__ long int  lroundf(float x);
__device__ long long int llrintf(float x);
__device__ long long int llroundf(float x);

/* TABLE C-2 */
__device__ double rsqrt(double x);
__device__ double sqrt(double x);
__device__ double cbrt(double x);
__device__ double rcbrt(double x);
__device__ double hypot(double x, double y);
__device__ double exp(double x);
__device__ double exp2(double x);
__device__ double exp10(double x);
__device__ double expm1(double x);
__device__ double log(double x);
__device__ double log2(double x);
__device__ double log10(double x);
__device__ double log1p(double x);
__device__ double sin(double x);
__device__ double cos(double x);
__device__ double tan(double x);
__device__ void   sincos(double x, double *sptr, double *cptr);
__device__ double sinpi(double x);
__device__ double cospi(double x);
__device__ double asin(double x);
__device__ double acos(double x);
__device__ double atan(double x);
__device__ double atan2(double y, double x);
__device__ double sinh(double x);
__device__ double cosh(double x);
__device__ double tanh(double x);
__device__ double asinh(double x);
__device__ double acosh(double x);
__device__ double atanh(double x);
__device__ double pow(double x, double y);
__device__ double erf(double x);
__device__ double erfc(double x);
__device__ double erfinv(double x);
__device__ double erfcinv(double x);
__device__ double erfcx(double x);
__device__ double lgamma(double x);
__device__ double tgamma(double x);
__device__ double fma(double x, double y, double z);

__device__ int bugle_frexp_exp(double x);
__device__ double bugle_frexp_frac(double x);

static __attribute__((always_inline)) __device__ double frexp(double x, int *exp) {
  *exp = bugle_frexp_exp(x);
  return bugle_frexp_frac(x);
}

__device__ double ldexp(double x, int exp);
__device__ double scalbn(double x, int n);
__device__ double scalbln(double x, int l);
__device__ double logb(double x);
__device__ int    ilogb(double x);
__device__ double j0(double x);
__device__ double j1(double x);
__device__ double jn(double x);
__device__ double y0(double x);
__device__ double y1(double x);
__device__ double yn(int x, double y);
__device__ double fmod(double x, double y);
__device__ double remainder(double x, double y);
__device__ double remquo(double x, double y, int *iptr);
__device__ double modf(double x, double *iptr);
__device__ double fdim(double x, double y);
__device__ double trunc(double x);
__device__ double round(double x);
__device__ double rint(double x);
__device__ double nearbyint(double x);
__device__ double ceil(double x);
__device__ double floor(double x);
__device__ long int lrint(double x);
__device__ long int  lround(double x);
__device__ long long int llrint(double x);
__device__ long long int llround(double x);

/* Table C-3 and C-4 */
__device__ float __fdividef(float x, float y);
__device__ float __sinf(float x);
__device__ float __cosf(float x);
__device__ float __tanf(float x);
__device__ float __sincosf(float x, float *sptr, float *cptr);
__device__ float __logf(float x);
__device__ float __log2f(float x);
__device__ float __log10f(float x);
__device__ float __expf(float x);
__device__ float __exp10f(float x);
__device__ float __powf(float x, float y);

__device__ float __fadd_rn(float x, float y);
__device__ float __fadd_rz(float x, float y);
__device__ float __fadd_ru(float x, float y);
__device__ float __fadd_rd(float x, float y);
__device__ float __fmul_rn(float x, float y);
__device__ float __fmul_rz(float x, float y);
__device__ float __fmul_ru(float x, float y);
__device__ float __fmul_rd(float x, float y);
__device__ float __fmaf_rn(float x, float y, float z);
__device__ float __fmaf_rz(float x, float y, float z);
__device__ float __fmaf_ru(float x, float y, float z);
__device__ float __fmaf_rd(float x, float y, float z);
__device__ float __frcp_rn(float x);
__device__ float __frcp_rz(float x);
__device__ float __frcp_ru(float x);
__device__ float __frcp_rd(float x);
__device__ float __fsqrt_rn(float x);
__device__ float __fsqrt_rz(float x);
__device__ float __fsqrt_ru(float x);
__device__ float __fsqrt_rd(float x);
__device__ float __fdiv_rn(float x, float y);
__device__ float __fdiv_rz(float x, float y);
__device__ float __fdiv_ru(float x, float y);
__device__ float __fdiv_rd(float x, float y);
// __dividef above
// __expf above
// __exp10f above
// __logf above
// __log2f above
// __log10f above
// __sinf above
// __cosf above
// __sincosf above
// __tanf above
// __powf above

/* Table C-5 */
__device__ double __dadd_rn(double x, double y);
__device__ double __dadd_rz(double x, double y);
__device__ double __dadd_ru(double x, double y);
__device__ double __dadd_rd(double x, double y);
__device__ double __dmul_rn(double x, double y);
__device__ double __dmul_rz(double x, double y);
__device__ double __dmul_ru(double x, double y);
__device__ double __dmul_rd(double x, double y);
__device__ double __fma_rn(double x, double y, double z);
__device__ double __fma_rz(double x, double y, double z);
__device__ double __fma_ru(double x, double y, double z);
__device__ double __fma_rd(double x, double y, double z);
__device__ double __ddiv_rn(double x, double y);
__device__ double __ddiv_rz(double x, double y);
__device__ double __ddiv_ru(double x, double y);
__device__ double __ddiv_rd(double x, double y);
__device__ double __drcp_rn(double x);
__device__ double __drcp_rz(double x);
__device__ double __drcp_ru(double x);
__device__ double __drcp_rd(double x);
__device__ double __dsqrt_rn(double x);
__device__ double __dsqrt_rz(double x);
__device__ double __dsqrt_ru(double x);
__device__ double __dsqrt_rd(double x);
