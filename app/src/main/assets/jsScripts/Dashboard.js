var ChildProfileCardClick = (e) =>{
    localStorage.setItem('ChildDetails', $(e).find('.ChildDetails').text())
    window.location.href = 'Temp.html';
}
$(document).ready(async ()=>{
    var ChildDetails = await AjaxScripts('FetchChildDetails', { ParentID:JSON.parse(localStorage.getItem('ParentDetails'))._id }, 'FetchChildDetails');
    var ChildCardTheme = "";
    ChildDetails.data.forEach((value, index)=>{
        ChildCardTheme += '<div class="col-xxl-4 col-md-6" onclick="ChildProfileCardClick(this)"><font class="ChildDetails d-none">'+(JSON.stringify(value))+'</font><div class="card info-card sales-card"> <div class="filter"> <a class="icon" href="#" data-bs-toggle="dropdown"><i class="bi bi-three-dots"></i></a> <ul class="dropdown-menu dropdown-menu-end dropdown-menu-arrow"> <li class="dropdown-header text-start"> <h6>Filter</h6> </li> <li><a class="dropdown-item" href="#">Today</a></li> <li><a class="dropdown-item" href="#">This Month</a></li> <li><a class="dropdown-item" href="#">This Year</a></li> </ul> </div> <div class="card-body"> <h5 class="card-title">'+value.ChildName+' <span></span></h5> <div class="d-flex align-items-center"> <div class="card-icon rounded-circle d-flex align-items-center justify-content-center"> <i class="bi bi-person"></i> </div> <div class="ps-3"> <h6>SCORE</h6> <span class="text-success small pt-1 fw-bold">%</span> <span class="text-muted small pt-2 ps-1">increase</span> </div> </div> </div> </div> </div>'
    })
    $('#ChildCardThemeDiv').append(ChildCardTheme)
})