# build package busybox-mdev and busybox's telnetd, needed by initramfs scripts
FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

SRC_URI += " \
    file://udhcpd.service \
"

do_install:append() {
    install -m 0644 ${WORKDIR}/udhcpd.service ${D}${systemd_system_unitdir}/
}

FILES:${PN} += "${systemd_system_unitdir}/udhcpd.service"