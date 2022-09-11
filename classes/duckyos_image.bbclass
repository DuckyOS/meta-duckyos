FEATURE_PACKAGES_duckyos-extended = "packagegroup-duckyos-extended"
FEATURE_PACKAGES_duckyos-development = "packagegroup-duckyos-development"

DUCKYOS_IMAGE_DEFAULT_FEATURES = "debug-tweaks ssh-server-openssh package-management duckyos-extended"

# We're using devtmpfs so are not required to have a minimal populated /dev in our images
USE_DEVFS = "1"

IMAGE_INSTALL ?= '\
    packagegroup-core-boot \
    \
    '

inherit core-image

IMAGE_INSTALL:append = " \
  ${MACHINE_EXTRA_RDEPENDS} \
"