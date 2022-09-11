SUMMARY = "Device State Management Entity"
LICENSE = "LGPL-2.1-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=2d5025d4aa3495befef8f17206a5b0a1"

PV = "0.84.0+git${SRCPV}"
SRCREV_FORMAT = "dbus-glib"
SRCREV = "d1518176a68ce416fd19515c0b88da2b48ce606a"
SRCREV_dbus-glib = "d42176ae4763e5288ef37ea314fe58387faf2005"

DEPENDS = "glib-2.0 libcheck mce-headers libdsme usb-moded systemd doxygen-native libngf libiphb cryptsetup"
RDEPENDS:${PN} = "glib-2.0 libngf libiphb mce systemd"

SRC_URI = " \
    git://github.com/sailfishos/dsme.git;protocol=https;branch=master \
    git://github.com/sailfishos-mirror/dbus-glib.git;protocol=https;branch=master;destsuffix=git/dbus-gmain;name=dbus-glib \
"

inherit pkgconfig autotools-brokensep systemd

S = "${WORKDIR}/git"

EXTRA_OECONF:prepend = " \
	--disable-poweron-timer \
	--disable-static \
	--disable-upstart \
	--enable-runlevel \
	--enable-systemd \
	--enable-pwrkeymonitor \
	--disable-validatorlistener \
	--enable-abootsettings \
	--enable-usewheel \
"

do_install:append() {
	mkdir -p ${D}/${systemd_system_unitdir}/
	sed -e "s|@LIBDIR@|${libdir}|g" ${S}/rpm/dsme.service.in > ${D}/${systemd_system_unitdir}/dsme.service
    rm -rf "${D}/opt"
}

FILES:${PN} += " \
    ${libdir}/dsme/* \
    ${sysconfdir}/dbus-1/system.d/dsme.conf \
    ${systemd_system_unitdir}/dsme.service \
    /etc/profile.d/reboot-via-dsme.sh \
    /usr/lib/startup/preinit/set_system_time \
"

SYSTEMD_SERVICE:${PN} = "dsme.service"
