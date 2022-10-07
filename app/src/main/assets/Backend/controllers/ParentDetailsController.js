var ParentDetails=require('../modals/ParentDetailsSchema');

exports.ParentDetailsInsUp=async(req,res)=>{
    try{
        var data = req.body;
        var CheckExist = await ParentDetails.find({ Email:data.Email, MobileNumber:data.MobileNumber });
        if(CheckExist.length){
            var InsUp = await ParentDetails.updateOne({ _id:CheckExist[0]._id },{ $set:data });
        }
        else
            InsUp = await ParentDetails.create(data)
        if(InsUp)
            return res.status(200).json({ "data":"", notifier:"success", Message:"Parent Details Successfully" })
        else
            return res.status(200).json({ "data":"", notifier:"error", Message:"Something went wrong!" })
    }
    catch(err){
        console.log(err)
    }
}