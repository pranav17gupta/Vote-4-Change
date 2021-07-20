function addvote()
{
      var id= $('input[type=radio][name=flat]:checked').attr('id');
    data={candidateid:id};
    $.post("AddVoteControllerServlet",data,processresponse);
}
function processresponse(responseText)
{
    responseText=responseText.trim();
    if(responseText=="success")
        window.location="votingresponse.jsp";
    else
        window.location="accessdenied.html";
}
