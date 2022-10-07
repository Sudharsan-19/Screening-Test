var Questions = [];
var Score = [];
let shuffle = (array) => {
	let currentIndex = array.length,  randomIndex;
	while (currentIndex != 0) {
		randomIndex = Math.floor(Math.random() * currentIndex);
		currentIndex--;
		[array[currentIndex], array[randomIndex]] = [array[randomIndex], array[currentIndex]];
	}
	return array;
}

let SelectImage = (e) =>{
	$('img').removeAttr('style')
	$('img').removeAttr('ImageSelected')
	$(e).css({ "border":"5px solid" }).attr('ImageSelected', true)
}

let SelectButton = (e) =>{
	$('input[type="button"]').removeAttr('style')
	$('input[type="button"]').removeAttr('ButtonSelected')
	$(e).css({ "border":"2px solid" }).attr('ButtonSelected', true)
}

let SetValueToText = (e) =>{
	$('.SpellingButtonText').filter((e, i)=>{ return $(i).val() === ''}).eq(0).val($(e).text())
}

let uploadWritingFile = async () =>{
	var formData = new FormData();
	// console.log(formData)
	formData.append('sampleFile', $('#sampleFile').get(0).files[0], JSON.parse(localStorage.ChildDetails)._id+""+(new Date().getTime())+"."+($('#sampleFile').get(0).files[0].name.split(/[#?]/)[0].split('.').pop().trim()));
	var ReturnData = await $.ajax({
		type: "POST",
		url: 'http://localhost:5000/api/UploadImagesToPath',
		headers: { 'IsAjax': 'true' },
		dataType: "json",
		processData: false,
		contentType: false,
		data: formData,
		success: function (data) {console.log("data : ",data)
			return data;
		},
		error: function (error) {
			console.log(error)
		}
	});
	return ReturnData;
}

let MultiFunctionAudio = async () =>{
	var AudioSound = $('audio').attr('AudioSound').split('-')
	$('audio').attr("src", 'https://translate.google.com/translate_tts?ie=UTF-&&client=tw-ob&tl='+('en')+'&q='+AudioSound[0])
    $('audio')[0].play()
    setTimeout(()=>{
        $('audio').attr("src", 'https://translate.google.com/translate_tts?ie=UTF-&&client=tw-ob&tl='+('en')+'&q='+AudioSound[1])
        $('audio')[0].play()
    },1500)
}

let CheckAnswer = async (QuestionType) =>{
	var Correctness, UserAnswer;
	const search = obj => obj.Type === QuestionType;
	var ScoreIndex = Score.findIndex(search);
	switch(QuestionType){
		case 'Riddles':
			case 'Familiar sounds':
			UserAnswer = $('img').length ? $('.ImageOptions[ImageSelected="true"]').attr('src').split('/').pop() : $('input[name="Answer"]:checked').val()
			if(typeof UserAnswer === 'undefined')
				window.notifications('error', 'Please Select an Image', 'CenterBig');
			var CheckCorrectness = $('#Answer').val().trim().toUpperCase() === UserAnswer.trim().toUpperCase();
			Correctness = CheckCorrectness;
		break;

		case 'Same Or Different':
			UserAnswer = $('input[type="button"][ButtonSelected="true"]').val();
			var CheckCorrectness = $('#Answer').val().trim().toUpperCase() === UserAnswer.trim().toUpperCase();
			Correctness = CheckCorrectness;
		break;

		case 'Audio Comprehension':
		case 'NormalCalculations':
		case 'MissingNumbers':
		case 'Shapes':
		case 'Symbols':
		case 'Pronounce':
		case 'Sentence':
		case 'ImageReading':
			UserAnswer = $('input[name="Answer"]').is(':visible') ? $('input[name="Answer"]:checked').val() : $('#final_span').text();
			var CheckCorrectness = $('#Answer').val().trim().toUpperCase() === UserAnswer.trim().toUpperCase();
			Correctness = CheckCorrectness;
		break;
		case 'Spelling':
			UserAnswer = $('.SpellingButtonText').map((e, i)=>{ return $(i).val() }).toArray().join('');
			var CheckCorrectness = $('#Answer').val().trim().toUpperCase() === UserAnswer.trim().toUpperCase();
			Correctness = CheckCorrectness;
		break;
		case 'Sequence Problem':
			if(QuestionType === 'Ordering')
				var Answer = $('.order-words-ul').children().toArray().map((e)=>{ return $(e).text() });
			else if(QuestionType === 'Sequence Problem')
				Answer = $('.order-pics').find('img').toArray().map((elem, index)=>{ return (elem.src.split('/').pop()) });
			var TrueAnswer = JSON.parse($('#Answer').val());
			var CheckCorrectness = (Answer.length == TrueAnswer.length) && Answer.every(function(element, index) {
				return element === TrueAnswer[index]; 
			});
			Correctness = CheckCorrectness;
		break;

		case 'Audio Listening':
			UserAnswer = $('.ImageOptions[ImageSelected="true"]').attr('src').split('/').pop();
			var CheckCorrectness = $('#Answer').val().trim().toUpperCase() === UserAnswer.trim().toUpperCase();
			Correctness = CheckCorrectness;
		break;
		/* END */ 
		case 'Ordering':
		
		case 'TextToImage':
		case 'AudioToImage':
		case 'ImageToImage':
			var UploadResult = await uploadWritingFile();
			if(UploadResult.data === "Success"){
				var ImageID = UploadResult.ImageID
			}
		break;
	}
	var Pusher = Questions[Number($('#QuestionCaption').attr('QuestionIndex'))];
	if(['TextToImage', 'AudioToImage', 'ImageToImage'].includes(QuestionType)){
		UserAnswer = ImageID
		Pusher.Score = 0;
	}
	else
		Pusher.Score = Correctness ? 1 : -1;
	console.log(Pusher.Score)
	Pusher.UserAnswer = UserAnswer;
	Score.push(Pusher)
	console.log(Score)
}

$(document).ready(async ()=>{
	$('.cover svg, .play').css({ top:"35%", left:"90%" })
	var AjaxResult = await AjaxScripts('FetchQuestionsForChildAssessment', { Grade:2 }, 'FetchQuestionsForChildAssessment');
	Questions = AjaxResult.data;
	$('#NextButton').click()
})

$('#NextButton').click(function () {
	if($('#QuestionCaption').attr('QuestionType') === 'TextMCQ'){
		if(!$('input[name="Answer"]:checked').length){
			window.notifications('info', 'Please Select an Option', 'Corner')
			return
		}
	}
	if($('#QuestionCaption').attr('QuestionType') === 'Sentence'){
		if($('#final_span').text() === ''){
			window.notifications('info', 'Please Record your voice', 'Corner')
			return
		}
	}
	$('.VoiceRecognitionDiv, #AudioPlayDiv, #UploadFileDiv').hide()
	if(typeof $('#QuestionCaption').attr('QuestionType') !== 'undefined')
		CheckAnswer($('#QuestionCaption').attr('QuestionType'))
	var panCount = Number($('#QuestionCaption').attr('QuestionIndex'));
	$('#QuestionCaption').attr('QuestionIndex', panCount+1);
    var curQues = Questions[panCount + 1];
	if(panCount + 1 === Questions.length){
		return
	}
	var quesType = curQues.Type;
	$('#QuestionCaption').attr('QuestionType', quesType)
	$('#QuestionCaption, #DisplayQuestion').empty()
	switch (quesType) {
		case 'NormalCalculations':
		case 'MissingNumbers':
		case 'Symbols':
			$('#QuestionCaption').text(curQues.QuestionSet.QuestionCaption)
			$('#Answer').val(curQues.QuestionSet.Answer[0])
			var CodingString = '<b>'+curQues.QuestionSet.Question+'</b><br/><br/>';
			CodingString += '<div class="row mb-3">';
			shuffle(curQues.QuestionSet.Options).forEach((value, index)=>{
				CodingString += '<div class="col-6"><input type="radio" name="Answer" value="'+value+'" /> '+value+'</div>';
				if(index % 2 === 1){
					CodingString += '</div><div class="row mb-3">'
				}
			})
			$('#DisplayQuestion').empty().append(CodingString)
		break;

		case 'Shapes':
			$('#QuestionCaption').text(curQues.QuestionSet.QuestionCaption)
			$('#Answer').val(curQues.QuestionSet.Answer[0])
			var CodingString = '<br/><img src="AssessmentImages/'+curQues.Category+'/'+curQues.QuestionSet.Question+'" width="35%"/b><br/><br/>';
			CodingString += '<div class="row mb-3">';
			shuffle(curQues.QuestionSet.Options).forEach((value, index)=>{
				CodingString += '<div class="col-6"><input type="radio" name="Answer" value="'+value+'" /> '+value+'</div>';
				if(index % 2 === 1){
					CodingString += '</div><div class="row mb-3">'
				}
			})
			$('#DisplayQuestion').empty().append(CodingString)
		break;

		case 'AudioToImage':
			$('#AudioPlayDiv').show()
			$('#QuestionCaption').text(curQues.QuestionSet.QuestionCaption)
			var CodingString = '';
			CodingString += '<br/>'
			if(curQues.QuestionSet.InputType === "Audio"){
				$('audio').attr({ "src":'https://translate.google.com/translate_tts?ie=UTF-&&client=tw-ob&tl='+('en')+'&q='+curQues.QuestionSet.Question, "FeedType":"Single" })
			}
			$('#UploadFileDiv').show()
			$('#Answer').val(curQues.QuestionSet.Answer[0])
			$('#DisplayQuestion').empty().append(CodingString)
		break;
		
		case 'TextToImage':
			$('#QuestionCaption').text(curQues.QuestionSet.QuestionCaption)
			$('#Question').text(curQues.QuestionSet.Question)
			var CodingString = '';
			CodingString += '<h4>'+curQues.QuestionSet.Question+'</h4><br/>'
			if(curQues.QuestionSet.OutputType === "Image"){
				$('#UploadFileDiv').show()
			}
			$('#Answer').val(curQues.QuestionSet.Answer[0])
			$('#DisplayQuestion').empty().append(CodingString)
		break;

		case 'ImageToImage':
			$('#QuestionCaption').text(curQues.QuestionSet.QuestionCaption)
			var CodingString = '';
			CodingString += '<br/><br/>'
			if(curQues.QuestionSet.InputType === "Image"){
				shuffle(curQues.QuestionSet.Question).forEach((value, index)=>{
					CodingString += '<div class="row mb-4"><div class="col-12"><img src="AssessmentImages/'+quesType+'/'+value+'" width="45%" /></div></div>'
				})
				CodingString += "</div>";
			}
			if(curQues.QuestionSet.OutputType === "Image"){
				$('#UploadFileDiv').show()
			}
			$('#Answer').val(curQues.QuestionSet.Answer[0])
			$('#DisplayQuestion').empty().append(CodingString)
		break;

		case 'Sequence Problem':
			$('#QuestionCaption').text(curQues.QuestionSet.QuestionCaption)
			var CodingString = '<div class="panel-answer order-pics">';

			if(curQues.QuestionSet.InputType === "Image"){
				shuffle(curQues.QuestionSet.Question).forEach((value, index)=>{
					CodingString += '<div class="row mb-4"><div class="col-12"><img src="AssessmentImages/Sequence Problem/'+value+'" /></div></div>'
				})
				CodingString += "</div>";
			}
			else
				CodingString += curQues.QuestionSet.Question+'<br/>'
			CodingString += '<div class="row mb-3">';
			
			shuffle(curQues.QuestionSet.Options).forEach((value, index)=>{
				CodingString += '<div class="col-6"><input type="radio" name="Answer" value="'+value+'" /> '+value+'</div>';
				if(index % 2 === 1){
					CodingString += '</div><div class="row mb-3">'
				}
			})
			$('#DisplayQuestion').empty().append(CodingString)
			$('.order-pics').sortable({
				items: "> .row",
				connectWith: ".order-pics",
				appendTo: '.order-pics',
				axis: 'y',
				helper: 'clone',
				cancel: '',
				opacity: 0.5,
				tolerance: 'pointer',
				containment: 'window'
			});
			$('#Answer').val(JSON.stringify(curQues.QuestionSet.Answer))
		break;

		case 'Riddles':
			$('#QuestionCaption').text(curQues.QuestionSet.QuestionCaption)
			var CodingString = '';
			CodingString += '<b>'+curQues.QuestionSet.Question+'</b><br/><br/>';
			shuffle(curQues.QuestionSet.Options).forEach((value, index)=>{
				if(value.includes('png'))
					CodingString += '<div class="row mb-3"><div class="col-12"><img src="AssessmentImages/'+$('#QuestionCaption').attr('QuestionType')+'/'+value+'" class="ImageOptions" onclick="SelectImage(this)"/></div></div>';
				else
					CodingString += '<div class="row mb-3"><div class="col-12"><input type="radio" value="'+value+'" name="Answer" id="Option'+index+'" /> <label for="Option'+index+'">'+value+'</label></div></div>';
			})
			$('#DisplayQuestion').empty().append('<br/>'+CodingString)
			$('#Answer').val(curQues.QuestionSet.Answer[0])
		break;
		
		case 'Same Or Different':
			$('#QuestionCaption').text(curQues.QuestionSet.QuestionCaption)
			$('#Answer').val(curQues.QuestionSet.Answer[0])
			$('#AudioPlayDiv').show()
			var CodingString = '<br/>';
			CodingString += '<div class="row mb-3">';
			shuffle(curQues.QuestionSet.Options).forEach((value, index)=>{
				CodingString += '<div class="col-6"><input type="button" name="Answer" value="'+value+'" class="btn btn-info" onclick="SelectButton(this)"/></div>';
				if(index % 2 === 1){
					CodingString += '</div><div class="row mb-3">'
				}
			})
			$('#DisplayQuestion').empty().append(CodingString)
			if(curQues.QuestionSet.InputType === "Audio"){
				$('audio').attr({ "src":'https://translate.google.com/translate_tts?ie=UTF-&&client=tw-ob&tl='+('en')+'&q='+curQues.QuestionSet.Question, "FeedType":"Multiple", "FunctionCall":"MultiFunctionAudio()", "AudioSound":curQues.QuestionSet.Question })
			}
		break;

		case 'Audio Comprehension':
			$('#QuestionCaption').text(curQues.QuestionSet.QuestionCaption)
			$('#Answer').val(curQues.QuestionSet.Answer[0])
			$('#AudioPlayDiv').show()
			var CodingString = '<br/>';
			CodingString += '<div class="row mb-3">';
			shuffle(curQues.QuestionSet.Options).forEach((value, index)=>{
				CodingString += '<div class="col-6"><input type="radio" name="Answer" value="'+value+'" /> '+value+'</div>';
				if(index % 2 === 1){
					CodingString += '</div><div class="row mb-3">'
				}
			})
			$('#DisplayQuestion').empty().append(CodingString)
			if(curQues.QuestionSet.InputType === "Audio"){
				$('audio').attr({ "src":'https://translate.google.com/translate_tts?ie=UTF-&&client=tw-ob&tl='+('en')+'&q='+curQues.QuestionSet.Question[0], "FeedType":"Single", "AudioSound":curQues.QuestionSet.Question[0] })
			}
		break;

		case 'Audio Listening':
			$('#QuestionCaption').text(curQues.QuestionSet.QuestionCaption)
			$('#Answer').val(curQues.QuestionSet.Answer[0])
			$('#AudioPlayDiv').show()
			var CodingString = '<br/>';
			shuffle(curQues.QuestionSet.Options).forEach((value, index)=>{
				if(value.includes('png'))
					CodingString += '<div class="row mb-3"><div class="col-12"><img src="AssessmentImages/'+$('#QuestionCaption').attr('QuestionType')+'/'+value+'" class="ImageOptions" onclick="SelectImage(this)" width="25%"/></div></div>';
				else
					CodingString += '<div class="row mb-3"><div class="col-12"><input type="radio" value="'+value+'" name="Answer" id="Option'+index+'" /> <label for="Option'+index+'">'+value+'</label></div></div>';
			})
			$('#DisplayQuestion').empty().append(CodingString)
			if(curQues.QuestionSet.InputType === "Audio"){
				$('audio').attr({ "src":'https://translate.google.com/translate_tts?ie=UTF-&&client=tw-ob&tl='+('en')+'&q='+curQues.QuestionSet.Question[0], "FeedType":"Single", "AudioSound":curQues.QuestionSet.Question[0] })
			}
		break;

		case 'Spelling':
			$('#QuestionCaption').text(curQues.QuestionSet.Question)
			$('#Answer').val(curQues.QuestionSet.Answer[0])
			var CodingString = '<br/><table width="100%"><tr>';
			var ButtonsList = "";
			ButtonsList += '<div class="row mb-3">';
			shuffle(curQues.QuestionSet.Answer[0].split("")).forEach((value, index)=>{
				CodingString += '<td style="padding:10px 10px 10px 10px;" ><input type="button" class="form-control SpellingButtonText" value="" onclick="$(this).val(\'\')" /> </td>';
				ButtonsList += '<div class="col-6"><button class="form-control btn btn-info" SpellingButton="true" onclick="SetValueToText(this)">'+value+'</button></div>';
				if(index % 2 === 1){
					ButtonsList += '</div><div class="row mb-3">'
				}
			})
			CodingString += '</tr></table>'
			$('#DisplayQuestion').empty().append(CodingString+""+ButtonsList)
		break;
		
		case 'Pronounce':
		case 'Sentence':
		case 'ImageReading':
			$('#QuestionCaption').text(curQues.QuestionSet.QuestionCaption)
			$('#Answer').val(curQues.QuestionSet.Answer[0])
			var CodingString = '<b>'+curQues.QuestionSet.Answer[0]+'</b><br/>';
			if(quesType === 'ImageReading'){
				CodingString = '<div class="row mb-3"><div class="col-12"><img src="AssessmentImages/'+$('#QuestionCaption').attr('QuestionType')+'/'+curQues.QuestionSet.Question[0]+'" width="35%"/></div></div>';
			}
			$('#DisplayQuestion').empty().append(CodingString)
			$('.VoiceRecognitionDiv').show()
		break;

		case 'Familiar sounds':
			$('#QuestionCaption').text(curQues.QuestionSet.QuestionCaption)
			$('#Answer').val(curQues.QuestionSet.Answer[0])
			$('#AudioPlayDiv').show()
			var CodingString = '<br/>';
			shuffle(curQues.QuestionSet.Options).forEach((value, index)=>{
				if(value.includes('png'))
					CodingString += '<div class="row mb-3"><div class="col-12"><img src="AssessmentImages/'+$('#QuestionCaption').attr('QuestionType')+'/Images/'+value+'" class="ImageOptions" onclick="SelectImage(this)" width="25%"/></div></div>';
				else
					CodingString += '<div class="row mb-3"><div class="col-12"><input type="radio" value="'+value+'" name="Answer" id="Option'+index+'" /> <label for="Option'+index+'">'+value+'</label></div></div>';
			})
			$('#DisplayQuestion').empty().append(CodingString)
			if(curQues.QuestionSet.InputType === "Audio"){
				$('audio').attr({ "src":'AssessmentImages/'+$('#QuestionCaption').attr('QuestionType')+'/Audio/'+curQues.QuestionSet.Question[0], "FeedType":"Single" })
			}
		break

		case 'Ordering':
			var CodingString = '<ul class="order-words-ul">';
			shuffle(curQues.QuestionSet.Question).forEach((value, index)=>{
				CodingString += '<li>'+value+'</li>'
			})
			CodingString += "</ul>";
			$('#QuestionCaption').text(curQues.QuestionSet.QuestionCaption)
			$('#DisplayQuestion').empty().append(CodingString)
			$('.order-words-ul').sortable({ items: "> li",
				connectWith: ".order-words-ul",
				axis: 'x',
				cancel: '',
				opacity: 0.5,
				tolerance: 'pointer',
				containment: 'window'
			});
			$('#Answer').val(JSON.stringify(curQues.QuestionSet.Answer))
		break;

		case 'TextMCQ':
			$('#QuestionCaption').text(curQues.QuestionSet.QuestionCaption)
			var CodingString = '';
			if(curQues.QuestionSet.InputType === "Image"){
				curQues.QuestionSet.Question.forEach((value, index)=>{
					CodingString += '<div class="row mb-3"><div class="col-12"><img src="AssessmentImages/'+value+'" width="50%" /></div></div>'
				})
			}
			else
				CodingString += curQues.QuestionSet.Question+'<br/>'
			CodingString += '<div class="row mb-3">';
			shuffle(curQues.QuestionSet.Options).forEach((value, index)=>{
				CodingString += '<div class="col-6"><input type="radio" name="Answer" value="'+value+'" /> '+value+'</div>';
				if(index % 2 === 1){
					CodingString += '</div><div class="row mb-3">'
				}
			})
			$('#DisplayQuestion').empty().append(CodingString)
			$('#Answer').val(JSON.stringify(curQues.QuestionSet.Answer[0]))
		break;
		
		
		case 'Sentence':
			$('.VoiceRecognitionDiv').show()
			$('#QuestionCaption').text(curQues.QuestionSet.Question)
			$('#Answer').val(JSON.stringify(curQues.QuestionSet.Answer[0]))
		break;

		
	}
});