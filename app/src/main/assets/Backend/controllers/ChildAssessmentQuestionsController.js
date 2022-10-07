var ChildAssessmentQuestions = require('../modals/ChildAssessmentQuestionsSchema');
let formidable = require('formidable');
var ObjectId = require('mongoose').Types.ObjectId;

exports.FetchChildAssessmentReading = async (req, res) =>{
    try{
        var data = req.body;
        var Questions = await ChildAssessmentQuestions.find({ "QuestionSet.QuestionType":data.QuestionType });
        var QuestionCategory = Object.keys(Questions[0].QuestionSet).filter((e)=>{
            return typeof (Questions[0].QuestionSet[e]) === 'object'
        })
        var QuestionObject = {};
        QuestionCategory.forEach((value, index)=>{
            if(Questions[0].QuestionSet[value].GradewiseDataset[0].hasOwnProperty('Grade'))
                var TempObj = Questions[0].QuestionSet[value].GradewiseDataset.filter((e)=>e.Grade === Number(data.ChildDetails.Grade))[0].DataSet;
            else
                TempObj = Questions[0].QuestionSet[value].GradewiseDataset
            QuestionObject[value] = {
                QuestionCaption:Questions[0].QuestionSet[value].QuestionCaption,
                DataSet:(TempObj[Math.floor(Math.random() * TempObj.length)])
            };
        })
        return res.status(200).json({ data:QuestionObject })
    }
    catch(err){
        console.log(err);
    }
}

exports.FetchQuestionsForChildAssessment = async (req, res) =>{
    try{
        var data = req.body; data.Grade = Number(data.Grade);
        var QuestionsList = await ChildAssessmentQuestions.aggregate([
            // { $match:{ Type:"ImageReading" } },
            { $match:{ "GradewiseDataSet.Grade":data.Grade } },
            { $unwind:{ path: "$GradewiseDataSet", preserveNullAndEmptyArrays: true } },
            { $match:{ "GradewiseDataSet.Grade":data.Grade } },
            {
                $group:
                {
                    _id:{ Category:"$Category", Type:"$Type", Grade:"$GradewiseDataSet.Grade" },
                    QuestionSet:{ $push:"$GradewiseDataSet" }
                }
            }
        ]);
        console.log(QuestionsList)
        // // Trial
        // var Temp = await ChildAssessmentQuestions.aggregate([
        //     { $match:{ Cateory:"Maths" } }
        // ])
        // // End of Trial
        // console.log(Temp)
        var ResponseQuestions = [];
        QuestionsList.forEach((value, index)=>{
            var RandomQuestion = Math.floor(Math.random() * value.QuestionSet[0].DataSet.length);
            // console.log(value.QuestionSet[0])
            ResponseQuestions.push({
                Category:value._id.Category,
                Type:value._id.Type,
                QuestionSet:value.QuestionSet[0].DataSet[RandomQuestion]
            })
        })
        return res.status(200).json({ data:ResponseQuestions })
    }
    catch(err){
        console.log(err);
    }
}

exports.UploadImagesToPath = async (req, res) =>{
    try{
        var data = req.body;
        let sampleFile; let uploadPath;
        if (!req.files || Object.keys(req.files).length === 0) {
            return res.status(200).json({data:'No files were uploaded.'});
        }

        sampleFile = req.files.sampleFile;
        uploadPath = 'E:/SIH/' + sampleFile.name;
        console.log(data)
        await sampleFile.mv(uploadPath, function(err) {
            if (err){
                console.log(err)
            }
        });
        return res.status(200).json({data:"Success", ImageID:new ObjectId()});
    }
    catch(err){
        console.log(err);
    }
}