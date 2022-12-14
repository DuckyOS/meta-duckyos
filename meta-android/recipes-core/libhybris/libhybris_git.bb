DESCRIPTION = "Hybris is a solution that commits hybris, by allowing us to use \
bionic-based HW adaptations in glibc systems"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=3b83ef96387f14655fc854ddc3c6bd57"

SRCREV = "1b6090ad6e420fe2139685e0af54fd94edb7d049"
PV = "0.1.0+gitr${SRCPV}"
PR = "r1"
PE = "1"

SRC_URI = "git://github.com/libhybris/libhybris;branch=master;protocol=https \
    file://0001-wayland-egl.pc.in-bump-Version-from-libhybris-s-0.1..patch;patchdir=.. \
    file://0001-Add-EGL_OPENGL_ES3_BIT_KHR-define.patch;patchdir=.. \
"

S = "${WORKDIR}/git/hybris"

PACKAGE_ARCH = "${MACHINE_ARCH}"

# We need the android headers which are now provided for compatiblity reasons as external
# component. The android-headers are specific for the environment the android-system-image
# is build with and can differ between different machines.
DEPENDS += "virtual/android-headers wayland-native wayland"

EXTRA_OECONF += " \
    --enable-wayland \
    --with-default-egl-platform=wayland \
    --enable-trace \
    --enable-debug \
    --with-android-headers=${STAGING_INCDIR}/android \
"
EXTRA_OECONF:append:aarch64 = " \ 
    --enable-arch=arm64 \
"

# Only MACHINEs which provide virtual/android-headers can build this
COMPATIBLE_MACHINE = "^halium$"

PROVIDES += "virtual/libgles1 virtual/libgles2 virtual/egl virtual/mesa"

# most MACHINEs don't use libhybris and depend on mesa to provide *gl*
# Multiple .bb files are due to be built which each provide virtual/libgles1 (virtual/libgles2, virtual/egl)
EXCLUDE_FROM_WORLD = "1"

# brokensep because configure creates include/android symlink to with-android-headers in ${S} not ${B}
inherit autotools-brokensep pkgconfig
