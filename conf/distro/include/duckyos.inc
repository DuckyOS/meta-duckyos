require conf/distro/include/duckyos-features.inc
require conf/distro/include/no-static-libs.inc

IMAGE_VERSION_SUFFIX = "-${DISTRO_VERSION}"

BUILDCFG_VARS += "DATETIME"

DISTRO_EXTRA_RDEPENDS:append:qemuall = " kernel-module-vboxvideo kernel-module-vboxguest kernel-module-uvesafb kernel-module-virtio-gpu kernel-module-vmwgfx"

MACHINE_ESSENTIAL_EXTRA_RDEPENDS:remove:qemuall = "v86d"

KERNEL_MODULE_AUTOLOAD:append:qemux86 = " vmwgfx vboxguest vboxsf"
KERNEL_MODULE_PROBECONF:append:qemux86 = " vmwgfx"
KERNEL_MODULE_AUTOLOAD:append:qemux86-64 = " vmwgfx vboxguest vboxsf"
KERNEL_MODULE_PROBECONF:append:qemux86-64 = " vmwgfx"

# Prevent vboxvideo from loading, it conflicts with vmwgfx
KERNEL_MODULE_PROBECONF:append:qemux86 = " vboxvideo"
KERNEL_MODULE_PROBECONF:append:qemux86-64 = " vboxvideo"
module_conf_vboxvideo:qemux86 = "blacklist vboxvideo"
module_conf_vboxvideo:qemux86-64 = "blacklist vboxvideo"

USER_CLASSES ?= ""

INHERIT_DISTRO ?= "debian devshell sstate license remove-libtool buildstats buildstats-summary image-buildinfo"

WARN_QA:append = " version-going-backwards"
ERROR_QA:remove = " version-going-backwards"

DEFAULT_WARN_QA = "\
    libdir xorg-driver-abi \
    textrel incompatible-license files-invalid \
    infodir build-deps src-uri-bad symlink-to-sysroot multilib \
    invalid-packageconfig host-user-contaminated uppercase-pn patch-fuzz \
    mime mime-xdg unlisted-pkg-lics unhandled-features-check \
    missing-update-alternatives native-last missing-ptest \
    license-exists license-no-generic license-syntax license-format \
    license-incompatible license-file-missing obsolete-license \
"
ERROR_QA:append = " ${DEFAULT_WARN_QA}"
WARN_QA:remove = "${DEFAULT_WARN_QA}"

IMAGE_FSTYPES ?= "tar.gz"
IMAGE_FSTYPES := "${@ '${IMAGE_FSTYPES} wic.vmdk' if '${MACHINE}'.startswith('qemux86') else '${IMAGE_FSTYPES}' }"
IMAGE_FSTYPES := "${@ '${IMAGE_FSTYPES} rpi-sdimg.gz' if '${MACHINE}'.startswith('raspberrypi') else '${IMAGE_FSTYPES}' }"
IMAGE_ROOTFS_EXTRA_SPACE := "${@ '2097152' if '${MACHINE}'.startswith('qemux86') else '${IMAGE_ROOTFS_EXTRA_SPACE}' }"
IMAGE_FSTYPES := "${@ ' ${IMAGE_FSTYPES} '.replace(' tar.bz2 ',' tar.gz ') }"

# Redirect console output to keep it from spewing all over the VirtualBox window
# (This is added to qemux86 kernel cmdline by openembedded-core/meta/classes/syslinux.bbclass)
APPEND:append:qemuall = " console=ttyS0"

# The CONNECTIVITY_CHECK_URI's are used to test whether we can succesfully
# fetch from the network (and warn you if not). To disable the test set
# the variable to be empty.
CONNECTIVITY_CHECK_URIS ?= "https://github.com"

# TODO can check on all these host machines.
SANITY_TESTED_DISTROS ?= " \
    fedora-38 \n \
    fedora-37 \n \
    fedora-36 \n \
"

# no interactive resolving, better to fail and report later
PATCHRESOLVE = "noop"

# save statistical info on images
INHERIT += "buildhistory"

# enable Hash Equivalence Server
# read https://lwn.net/Articles/804640/ for details
BB_SIGNATURE_HANDLER ?= "OEEquivHash"
BB_HASHSERVE ??= "auto"

# to build stuff with commercial licenses
# LICENSE_FLAGS_ACCEPTED is now used instead of COMMERCIAL_LICENSE, see:
# http://git.openembedded.org/?p=openembedded-core.git&a=commit;h=a66fdbda548fab367cada035c49a32d9bf8ea528
# we are not commercial project, we can whitelist them all
LICENSE_FLAGS_ACCEPTED = "commercial"

LICENSE_CREATE_PACKAGE = "1"

ROOT_HOME = "/root"

#############################################################################
# Ensure MACHINE_CLASS is in OVERRIDES and is not empty as :: in OVERRIDES
# breaks order of OVERRIDES ie in FILESPATH
#############################################################################
MACHINE_CLASS ?= "${MACHINE}"
MACHINEOVERRIDES .= ":${MACHINE_CLASS}"
MACHINEOVERRIDES[vardepsexclude] = "MACHINE MACHINE_CLASS"
OVERRIDES[vardepsexclude] = "MACHINE MACHINE_CLASS"

SYSTEMD_DEFAULT_TARGET = "graphical.target"

inherit android_image_types
