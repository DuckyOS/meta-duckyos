SUMMARY = "USB mode controller (developer mode configuration)"
LICENSE = "LGPL-2.1-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=5f30f0716dfdd0d91eb439ebec522ec2"

PV = "0.66.7"

RDEPENDS:${PN} = "usb-moded"

SRC_URI = " \
    file://developer_mode.ini \
    file://LICENSE \
"

S = "${WORKDIR}"

do_install() {
    cd "${S}"
    install -d "${D}"/${sysconfdir}/usb-moded/dyn-modes
    install -m 644 -D "${S}"/developer_mode.ini "${D}"/${sysconfdir}/usb-moded/dyn-modes
}

FILES:${PN} += " \
    ${sysconfdir}/usb-moded/dyn-modes/developer_mode.ini \
"
