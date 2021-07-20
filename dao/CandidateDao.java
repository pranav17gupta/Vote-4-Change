/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evoting.dao;

import evoting.dbutil.DBConnection;
import evoting.dto.AddCandidateDto;
import evoting.dto.CandidateDto;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;

/**
 *
 * @author Soft2
 */
public class CandidateDao {
    private static Statement st,st2,st3;
    private static PreparedStatement ps,ps1,ps2,ps3,ps4;
    static
    {
        try {
            st=DBConnection.getConnection().createStatement();
            st2=DBConnection.getConnection().createStatement();
            st3=DBConnection.getConnection().createStatement();
            ps=DBConnection.getConnection().prepareStatement("select username from user_details where adhar_no=?");
            ps1=DBConnection.getConnection().prepareStatement("insert into candidate values(?,?,?,?,?)");
            ps2=DBConnection.getConnection().prepareStatement("select * from candidate where candidate_id=?");
            ps3=DBConnection.getConnection().prepareStatement("update candidate set party=?,election_symbol=?,city=? where candidate_id=?");
            ps4=DBConnection.getConnection().prepareStatement("delete from candidate where candidate_id=?");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    public static String getNewCandidateId()throws SQLException
    {
        ResultSet rs=st.executeQuery("select count(*) from candidate");
        if(rs.next())
            return "C"+(100+(rs.getInt(1)+1));
        return null;
    }
    public static String getUsernameById(String uid)throws SQLException
    {
        ps.setString(1, uid);
        ResultSet rs=ps.executeQuery();
        if(rs.next())
            return rs.getString(1);
        return null;
    }
    public static ArrayList<String> getCity()throws SQLException
    {
        ArrayList<String> city=new ArrayList<>();
        ResultSet rs=st2.executeQuery("select distinct city from user_details");
        while(rs.next())
            city.add(rs.getString(1));
        return city;
    }
    
    public static boolean addCandidate(AddCandidateDto candidate)throws SQLException
    {
        ps1.setString(1, candidate.getCandidateId());
        ps1.setString(2, candidate.getParty());
        ps1.setBinaryStream(3, candidate.getSymbol());
        ps1.setString(4, candidate.getCity());
        ps1.setString(5, candidate.getUserId());
        return (ps1.executeUpdate()!=0);
    }
    public static ArrayList<String> getCandidateId()throws SQLException
    {
        ResultSet rs=st3.executeQuery("select candidate_id from candidate");
        ArrayList<String> id=new ArrayList<>();
        while(rs.next())
        {
            id.add(rs.getString(1));
        }
        return id;
    }
    public static CandidateDetails getDetailsById(String cid)throws Exception
    {
        ps2.setString(1, cid);
        ResultSet rs=ps2.executeQuery();
        CandidateDetails candidate=new CandidateDetails();
        Blob blob ;
        InputStream inputStream;
                ByteArrayOutputStream outputStream;
                byte[] buffer;
                int bytesRead;
                byte[] imageBytes;
                String base64Image;
        if(rs.next())
        {
            blob=rs.getBlob(3);
            inputStream = blob.getBinaryStream();
            outputStream = new ByteArrayOutputStream();
            buffer = new byte[4096];
            bytesRead = -1;
             while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);                  
                }
             imageBytes = outputStream.toByteArray();
             base64Image = Base64.getEncoder().encodeToString(imageBytes);
             candidate.setSymbol(base64Image);
             candidate.setCandidateId(cid);
             candidate.setCandidateName(getUsernameById(rs.getString(5)));
             candidate.setParty(rs.getString(2));
             candidate.setUserId(rs.getString(5));
             candidate.setCity(rs.getString(4));
        }
        return candidate;
    }
    public static boolean updateCandidate(AddCandidateDto candidate)throws Exception
    {
        ps3.setString(1, candidate.getParty());
         ps3.setBinaryStream(2, candidate.getSymbol());
         ps3.setString(3, candidate.getCity());
         ps3.setString(4, candidate.getCandidateId());
        return (ps3.executeUpdate()!=0);
    }
    public static boolean removeCandidate(String cid)throws SQLException
    {
        ps4.setString(1, cid);
        return (ps4.executeUpdate()!=0);
    }
}
