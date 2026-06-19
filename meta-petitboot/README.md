# OpenEmbedded layer for Petitboot

## About

This layer provides

* `petitboot`: A small kexec-based bootloader
* `petitboot-image`: A tailored boot image/initramfs that runs petitboot

## Dependencies

* URI: `https://git.openembedded.org/openembedded-core`
    + branch: matched branches (e.g. `master`)
    + layer: `meta`

* URI: `https://git.openembedded.org/meta-openembedded`
    + branch: matched branches (e.g. `master`)
    + layer: `meta-oe`, `meta-networking`

## Patches

https://github.com/open-power/meta-openpower

## Adding the meta-petitboot layer to your build

Run `bitbake-layers add-layer meta-petitboot`
