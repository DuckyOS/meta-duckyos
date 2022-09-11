DESCRIPTION = "Basic set of components used by DuckyOS"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PACKAGE_ARCH = "${MACHINE_ARCH}"
inherit packagegroup

NOT_COMPATIBLE_WITH_CURRENT_NODEJS = " \
  node-sqlite3 \
"


RDEPENDS:${PN} = " \
  ${DISTRO_EXTRA_RDEPENDS} \
  distro-feed-configs \
  dsme mce ohm ngfd \
  lipstick-ducky-home \
  tar \
  iw \
  bluez5 \
  ca-certificates \
"

LIBHYBRIS_RDEPENDS = " \
    ${VIRTUAL-RUNTIME_android-system-image} \
    android-system \
    android-system-compat \
    android-tools \
    libhybris \
    udev-extraconf \
    lxc \
    exiv2 \
    qt5-qpa-hwcomposer-plugin \
"

RDEPENDS:${PN}:append:sargo = " ${LIBHYBRIS_RDEPENDS}"

QEMU_RDEPENDS = " \
    alsa-utils-systemd \
    mesa-driver-swrast \
    kernel-module-snd-intel8x0 \
    libpci \
    phonesim \
    qt-plugin-generic-vboxtouch \
    rng-tools \
    vmwgfx-layout \
"

RDEPENDS:${PN}:append:qemux86 = " ${QEMU_RDEPENDS}"
RDEPENDS:${PN}:append:qemux86-64 = " ${QEMU_RDEPENDS}"

RDEPENDS:${PN}:append:arm = " \
    crash-handler \
"
