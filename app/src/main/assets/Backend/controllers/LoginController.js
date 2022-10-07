var  ParentDetails=require('../modals/ParentDetailsSchema');


exports.VerifyLogin=async(req,res)=>{
try{
    var data=req.body;
    var CheckExist = await ParentDetails.find({ $and:[ { $or:[ { Email:data.Email },{ MobileNumber:data.Email } ] },{ Password:data.Password }] },{ FatherName:1, MotherName:1, MobileNumber:1, Email:1, MotherTongue:1, OtherLanguage:1 })
    if(CheckExist.length)    {
        return res.status(200).json({"data":CheckExist[0], notifier:"success", Message:"Welcome" })
    }
    else{
        return res.status(200).json({"data":"", notifier:"error", Message:"Sorry Email or Password did not match" })
    }
} 
catch(err){
    console.log(err)
}}