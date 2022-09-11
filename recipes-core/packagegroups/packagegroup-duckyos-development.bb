DESCRIPTION = "Development components used by DuckyOS"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PACKAGE_ARCH = "${MACHINE_ARCH}"
inherit packagegroup

RDEPENDS:${PN} = " \
  fingerterm \
  strace \
  vim \
  connman-client \
"

LIBHYBRIS_RDEPENDS = " \
    android-tools-adbd \
"

RDEPENDS:${PN}:append:sargo = " ${LIBHYBRIS_RDEPENDS}"