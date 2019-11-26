package lk.ijse.dep.pos.db;

import lk.ijse.deppo.crypto.DEPCrypt;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JPAUtil {

    private static EntityManagerFactory entityManager=buildEntityManager();
    private static String username;
    private static String password;
    private static String db;
    private static String port;
    private static String host;

    private  static EntityManagerFactory buildEntityManager(){

        File file = new File("resources/application.properties");

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            Properties properties = new Properties();
            properties.load(fileInputStream);


            username=DEPCrypt.decode(properties.getProperty("javax.persistence.jdbc.user"),"dep4");
            password= DEPCrypt.decode(properties.getProperty("javax.persistence.jdbc.password"),"dep4");
            db=properties.getProperty("ijse.dep.db");
            port=properties.getProperty("ijse.dep.port");
            host=properties.getProperty("ijse.dep.ip");

            properties.setProperty("javax.persistence.jdbc.user",username);
            properties.setProperty("javax.persistence.jdbc.password",password);

            return  Persistence.createEntityManagerFactory("dep4",properties);

        } catch (Exception e) {
            Logger.getLogger("lk.ijse.dep.pos.db.JPAUtil").log(Level.SEVERE,null,e);
            System.exit(1);
            return null;
        }

    }

    public static EntityManagerFactory getEntityManager(){
        return entityManager;
    }

    public static String getUsername() {
        return username;
    }


    public static String getPassword() {
        return password;
    }

    public static String getDb() {
        return db;
    }

    public static String getPort() {
        return port;
    }

    public static String getHost() {
        return host;
    }

}
