<%@page import="java.util.Iterator"%>
<%@page import="java.util.function.*"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="evoting.dao.CandidateDetails"%>
<%@page import="java.util.Map"%>
<%
     String userid=(String)session.getAttribute("userid");
    if(userid==null)
            {
                response.sendRedirect("accessdenied.html");
                return;
            }
    Map<CandidateDetails,Integer> result=(Map)request.getAttribute("result");
    int votecount=(int)request.getAttribute("votecount");
    Iterator i=result.entrySet().iterator();
    StringBuffer displayBlock = new StringBuffer("<table>");
    displayBlock.append("<tr><th>Candidate Id</th><th>Candidate Name</th><th>Party</th><th>Symbol</th><th>Voting Count</th><th>Vote %</th></tr>");
    int count=1;
    while(i.hasNext())
    {
        Map.Entry<CandidateDetails,Integer> e=(Map.Entry)i.next();
        displayBlock.append("<tr id='"+("tr"+count)+"' style='outline: thin solid'><td>"+e.getKey().getCandidateId()+"</td><td>"+e.getKey().getCandidateName()+"</td><td>"+e.getKey().getParty()+"</td><td><img src='data:image/jpg;base64,"+e.getKey().getSymbol()+"' style='width:300px;height:200px;'/></td><td>"+e.getValue()+"</td><td>"+((e.getValue()*100.0)/votecount)+"%</td></tr>");
        count++;
    }
    displayBlock.append("</table>");
    out.println(displayBlock);
%>