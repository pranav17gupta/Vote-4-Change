<%
    String userid=(String)session.getAttribute("userid");
    boolean result=(boolean)request.getAttribute("result");
    if(userid!=null&&result==true)
    out.println("success");
    else
    out.println("failed");
%>