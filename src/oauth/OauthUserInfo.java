/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oauth;

import FacebootNet.Packets.Server.SOauthPacket;

/**
 *
 * @author Ivy
 */
public class OauthUserInfo {
    
    public String id;
    public String name;
    public String email;
    public String firstName;
    public String lastName;
    public byte[] profilePicture;
    public String gender;
    public String birthday;
    
    public OauthUserInfo(){
        id = "";
        name = "";
        email = "";
        firstName = "";
        lastName = "";
        profilePicture = new byte[0];
        gender = "";
        birthday = "";
    }
    
    public void mapPacket(int oAuthType, SOauthPacket packet){
        packet.OauthType = oAuthType;
        packet.Id = id;
        packet.Name = name;
        packet.Email = email;
        packet.FirstName = firstName;
        packet.LastName = lastName;
        packet.Gender = gender;
        packet.BirthMonth = 0;
        packet.BirthDay = 0;
        packet.BirthYear =0 ;
        if (birthday != null && birthday.length() > 0){
        String[] dates = birthday.split("\\/");
        packet.BirthMonth = Short.parseShort(dates[0], 10);
        packet.BirthDay = Short.parseShort(dates[1], 10);
        packet.BirthYear = Short.parseShort(dates[2], 10);
        }
    }

    @Override
    public String toString() {
        return "OauthUserInfo{" + "id=" + id + ", name=" + name + ", email=" + email + ", firstName=" + firstName + ", lastName=" + lastName + ", profilePicture=" + profilePicture.length + ", gender=" + gender + '}';
    }
    
    
}
