SUMMARY = "EDGESCALE-EDS is a set of software agents running on device side which connects to cloud"
HOMEPAGE = "https://github.com/NXP/qoriq-edgescale-eds.git"
LICENSE = "NXP-EULA"
LIC_FILES_CHKSUM = "file://EULA.txt;md5=ac5425aaed72fb427ef1113a88542f89"

SRC_URI = "\
        git://${GO_IMPORT}.git;protocol=ssh;nobranch=1 \
        "
SRC_URI_append = " file://edgescale.service"

SRCREV = "7d70a8767941aed135d609300a0594dfdc60e5ea"

GO_IMPORT = "github.com/NXP/qoriq-edgescale-eds"

S = "${WORKDIR}/git"
inherit systemd update-rc.d
DEPENDS_append = " update-rc.d-native"

INITSCRIPT_NAME = "edgescale" 
SYSTEMD_SERVICE_${PN} = "edgescale.service"

do_patch[noexec] = "1"
do_configure[noexec] = "1"
do_compile[noexec] = "1"
do_install() {
        install -d ${D}${sysconfdir}/init.d
        install -d ${D}${sysconfdir}/rcS.d
        install -m 0755  ${S}/etc/edgescale  ${D}${sysconfdir}/init.d
        update-rc.d -r ${D} edgescale  start 40 5 .

        if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
            install -d ${D}/usr/local/edgescale/bin
            install -m 0755  ${S}/etc/edgescale ${D}/usr/local/edgescale/bin
            install -D -p -m0644 ${WORKDIR}/edgescale.service ${D}${systemd_system_unitdir}/edgescale.service
        fi

}
INSANE_SKIP_${PN} += "file-rdeps"
FILES_${PN} += "/usr/local/*"
