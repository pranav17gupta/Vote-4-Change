/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evoting.dao;

import evoting.dbutil.DBConnection;
import evoting.dto.CandidateDto;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Soft2
 */
public class VoteDao {
    private static PreparedStatement ps,ps2,ps3;
    private static Statement st;
    static
    {
        try {
            st=DBConnection.getConnection().createStatement();
            ps=DBConnection.getConnection().prepareStatement("select candidate_id from voting where user_id=?");
            ps2=DBConnection.getConnection().prepareStatement("select  candidate_id,username,party,election_symbol from candidate inner join user_details on candidate.user_id=user_details.adhar_no where candidate_id=?");
            ps3=DBConnection.getConnection().prepareStatement("select candidate_id,count(*) from voting group by candidate_id");
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    public static String getCandidateId(String userid)throws SQLException
    {
        ps.setString(1, userid);
        ResultSet rs=ps.executeQuery();
        if(rs.next())
            return rs.getString(1);
        return null;
    }
    public static CandidateDto getVote(String candidateid)throws SQLException, IOException
    {
        CandidateDto candidate=null;
        Blob blob ;
        InputStream inputStream;
                ByteArrayOutputStream outputStream;
                byte[] buffer;
                int bytesRead;
                byte[] imageBytes;
                String cImage;
            ps2.setString(1, candidateid);
            ResultSet rs=ps2.executeQuery();
            if(rs.next())
            {
            blob=rs.getBlob(4);
            inputStream = blob.getBinaryStream();
            outputStream = new ByteArrayOutputStream();
            buffer = new byte[4096];
            bytesRead = -1;
             while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);                  
                }
             imageBytes = outputStream.toByteArray();
             cImage = Base64.getEncoder().encodeToString(imageBytes);
            candidate=new CandidateDto(rs.getString(1),rs.getString(2),rs.getString(3),cImage);
            }
        
        return candidate;
        
    }
    public static Map<String,Integer> getResult()throws Exception
    {
        HashMap<String,Integer> result=new HashMap<>();
        ResultSet rs=ps3.executeQuery();
        while(rs.next())
            result.put(rs.getString(1), rs.getInt(2));
        return result;
    }
    public static int getVoteCount()throws SQLException
    {
        ResultSet rs=st.executeQuery("select count(*) from voting");
        if(rs.next())
            return rs.getInt(1);
        return 0;
    }
}
