$("#especialidade").autocomplete({
    source: function (request, response) {
        $.ajax({
            method: "GET",
            url: "/especialidades/titulo",
            data: {
            	termo: request.term
			},
            success: function (data) {
            	response(data);
            }
        });
    }
});


$('#especialidade').on('blur', function() {
    $('div').remove(".custom-radio");
	var titulo = $(this).val();
	if ( titulo != '' ) {			
		$.get( "/medicos/especialidade/titulo/" + titulo , function( result ) {
				
			var ultimo = result.length - 1; 
			
			$.each(result, function (k, v) {
				
				if ( k == ultimo ) {
	    			$("#medicos").append( 
	    				 '<div class="custom-control custom-radio">'	
	    				+  '<input class="custom-control-input" type="radio" id="customRadio'+ k +'" name="medico.id" value="'+ v.id +'" required>'
						+  '<label class="custom-control-label" for="customRadio'+ k +'">'+ v.nome +'</label>'
						+  '<div class="invalid-feedback">Médico é obrigatório</div>'
						+'</div>'
	    			);
				} else {
	    			$("#medicos").append( 
	    				 '<div class="custom-control custom-radio">'	
	    				+  '<input class="custom-control-input" type="radio" id="customRadio'+ k +'" name="medico.id" value="'+ v.id +'" required>'
						+  '<label class="custom-control-label" for="customRadio'+ k +'">'+ v.nome +'</label>'
						+'</div>'
	        		);	            				
				}
		    });
		});
	}
});	

$('#data').on('blur', function () {
	$("#horarios").empty();
    var data = $(this).val();
    var medico = $('input[name="medico.id"]:checked').val();
    if (!Date.parse(data)) {
        console.log('data nao selecionada')
    } else {
    	$.get('/agendamentos/horario/medico/'+ medico + '/data/' + data , function( result ) {
    		$.each(result, function (k, v) {
    			$("#horarios").append( 
    				'<option class="op" value="'+ v.id +'">'+ v.horaMinuto + '</option>'
    			);	            			
    	    });
    	});
    }
});
