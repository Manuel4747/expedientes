Number.prototype.formatMoney = function (c, d, t) {
    var n = this,
            c = isNaN(c = Math.abs(c)) ? 2 : c,
            d = d == undefined ? "," : d,
            t = t == undefined ? "." : t,
            s = n < 0 ? "-" : "",
            i = String(parseInt(n = Math.abs(Number(n) || 0).toFixed(c))),
            j = (j = i.length) > 3 ? j % 3 : 0;
    return s + (j ? i.substr(0, j) + t : "") + i.substr(j).replace(/(\d{3})(?=\d)/g, "$1" + t) + (c ? d + Math.abs(n - i).toFixed(c).slice(2) : "");
};

function calcularMontoCuota2() {
    document.getElementById('PreciosLoteCreateForm:montoCuota').value = (document.getElementById('PreciosLoteCreateForm:precioTotalCuotas').value / document.getElementById('PreciosLoteCreateForm:nroCuotas').value).formatMoney(0);

}

function cambiar() {
    alert("Cuot: " + document.getElementById('VentasLoteCreateForm:precioContado').value);
    if( document.getElementById('VentasLoteCreateForm:condicionVenta').value === 'CO' ){
        document.getElementById('VentasLoteCreateForm:precioContado').disabled = true;
        document.getElementById('VentasLoteCreateForm:descuentoContado').disabled = true;
        document.getElementById('VentasLoteCreateForm:precioTotalCuotas').disabled = false;
        document.getElementById('VentasLoteCreateForm:nroCuotas').disabled = false;
        document.getElementById('VentasLoteCreateForm:montoCuota').disabled = false;
    }else if( document.getElementById('VentasLoteCreateForm:condicionVenta').value === 'CU' ){
        document.getElementById('VentasLoteCreateForm:precioContado').disabled = false;
        document.getElementById('VentasLoteCreateForm:descuentoContado').disabled = false;
        document.getElementById('VentasLoteCreateForm:precioTotalCuotas').disabled = true;
        document.getElementById('VentasLoteCreateForm:nroCuotas').disabled = true;
        document.getElementById('VentasLoteCreateForm:montoCuota').disabled = true;
    }else{
        document.getElementById('VentasLoteCreateForm:precioContado').disabled = false;
        document.getElementById('VentasLoteCreateForm:descuentoContado').disabled = false;
        document.getElementById('VentasLoteCreateForm:precioTotalCuotas').disabled = false;
        document.getElementById('VentasLoteCreateForm:nroCuotas').disabled = false;
        document.getElementById('VentasLoteCreateForm:montoCuota').disabled = false;
    }

}

var ajaxInProgress;
function startHandler(espera) {
    ajaxInProgress = setTimeout(function () {
        PF('statusDialog').show();
    }, espera);
}
function endHandler() {
    clearTimeout(ajaxInProgress);
    PF('statusDialog').hide();
    ajaxInProgress = null;
}
function monitorExporting(start, complete) {
    if (PrimeFaces.cookiesEnabled()) {

        PrimeFaces.setCookie('cookie.chart.exporting', null);

        if (start) {
            start();
        }

        window.chartExportingMonitor = setInterval(function () {
            var exportingComplete = PrimeFaces.getCookie('cookie.chart.exporting');

            if (exportingComplete === 'true') {
                if (complete) {
                    complete();
                }

                clearInterval(window.chartExportingMonitor);
                PrimeFaces.setCookie('cookie.chart.exporting', null);
            }
        }, 150);
    }
}


function getNewItemPos() {
        var noOfItems = $("#ExpActuacionesViewForm\\:hiddenSizeId").val();
        var itemIx = $("#ExpActuacionesViewForm\\:hiddenIxId").val();
        var bucketHt = $("#ExpActuacionesViewForm\\:datalistActuaciones .ui-datatable-scrollable-body").height();
        var scrollHt = $("#ExpActuacionesViewForm\\:datalistActuaciones .ui-datatable-scrollable-body").prop("scrollHeight");

        var itemHt = parseInt(scrollHt) / parseInt(noOfItems);
        var noItemsInBucket = parseInt(bucketHt) / parseFloat(itemHt);

        var itemNo = parseInt(itemIx) + 1;
        var targetBucket = parseInt(itemNo) / parseFloat(noItemsInBucket);
        var halfBucketHt = parseInt(bucketHt) / 2;

        var scrollPos = 
                (parseInt(bucketHt) * (parseFloat(targetBucket) - 1)) + parseFloat(halfBucketHt);
        var scrollPosInt = Math.round(parseFloat(scrollPos));

        $('#ExpActuacionesViewForm\\:datalistActuaciones .ui-datatable-scrollable-body').animate({scrollTop:scrollPosInt});
}

function getNewItemPosActaSesion() {
        var noOfItems = $("#ExpActasSesionListForm\\:hiddenSizeId").val();
        var itemIx = $("#ExpActasSesionListForm\\:hiddenIxId").val();
        var bucketHt = $("#ExpActasSesionListForm\\:datalistActasSesion .ui-datatable-scrollable-body").height();
        var scrollHt = $("#ExpActasSesionListForm\\:datalistActasSesion .ui-datatable-scrollable-body").prop("scrollHeight");

        var itemHt = parseInt(scrollHt) / parseInt(noOfItems);
        var noItemsInBucket = parseInt(bucketHt) / parseFloat(itemHt);

        var itemNo = parseInt(itemIx) + 1;
        var targetBucket = parseInt(itemNo) / parseFloat(noItemsInBucket);
        var halfBucketHt = parseInt(bucketHt) / 2;

        var scrollPos = 
                (parseInt(bucketHt) * (parseFloat(targetBucket) - 1)) + parseFloat(halfBucketHt);
        var scrollPosInt = Math.round(parseFloat(scrollPos));

        $('#ExpActasSesionListForm\\:datalistActasSesion .ui-datatable-scrollable-body').animate({scrollTop:scrollPosInt});
}
	