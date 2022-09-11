DESCRIPTION = "DuckyOS image (development version)"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

IMAGE_FEATURES += "${DUCKYOS_IMAGE_DEFAULT_FEATURES} duckyos-development"

duckyos_enable_devmode() {
    touch ${IMAGE_ROOTFS}/var/usb-debugging-enabled
    touch ${IMAGE_ROOTFS}/.writable_image
}

ROOTFS_POSTPROCESS_COMMAND += "duckyos_enable_devmode;"

inherit duckyos_image