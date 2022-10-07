var mangoose=require("mangoose")
const { Schema } = require("mongoose")

var Question = mangoose.schema({
    Question:{
        type:String,
        minLength: 10,
        maxLength:1000,
    },
    AnswerOptions:{
        QuestionID:
        Answer:
        type:String,
    },
})Schema Category.virtual('categoryID').length(funtion(){
    return this._id ;
})
module.exports=mongoose.model('Question',Question,'Question')