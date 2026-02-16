/*
$(document).keydown(function(e) {

  // Set self as the current item in focus
  var self = $(':focus'),
      // Set the form by the current item in focus
      form = self.parents('form:eq(0)'),
      focusable;

  // Array of Indexable/Tab-able items
  focusable = form.find('input,a,select,button,textarea,div[contenteditable=true]').filter(':visible');

  function enterKey(){
    if (e.which === 13 && !self.is('textarea,div[contenteditable=true]')) { // [Enter] key

      // If not a regular hyperlink/button/textarea
      if ($.inArray(self, focusable) && (!self.is('a,button'))){
        // Then prevent the default [Enter] key behaviour from submitting the form
        e.preventDefault();
      } // Otherwise follow the link/button as by design, or put new line in textarea

      // Focus on the next item (either previous or next depending on shift)
      focusable.eq(focusable.index(self) + (e.shiftKey ? -1 : 1)).focus();

      return false;
    }
  }
  // We need to capture the [Shift] key and check the [Enter] key either way.
  if (e.shiftKey) { enterKey() } else { enterKey() }
});

*/

function handleSubmit(xhr, status, args, dialog) {
    var jqDialog = jQuery('#'+dialog.id);
    if(args.validationFailed) {
        jqDialog.effect('shake', { times:3 }, 100);
    } else {
        dialog.hide();
    }
}function handleSubmit3(xhr, status, args, dialog) {
    var jqDialog = jQuery('#'+dialog.id);
    if(args.validationFailed) {
        jqDialog.effect('shake', { times:3 }, 100);
    }
}
function handleSubmit2(xhr, status, args, dialog, boton) {
    var jqDialog = jQuery('#'+dialog.id);
    if(args.validationFailed) {
        jqDialog.effect('shake', { times:3 }, 100);
    } else {
        var x =document.getElementsByClassName(boton);
        x[0].click();
    }
}
function fixPFMDialogs() {
    jQuery("body > div[data-role='page']").each(function (i) {
        var pageId = jQuery(this).attr("id");
        jQuery("body > div[id*='" + pageId + "'][class*='ui-popup']").appendTo("#" + pageId);
    });
}
