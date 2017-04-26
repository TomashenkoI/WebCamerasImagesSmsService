package com.kievstar.webcameras.impl;

import com.kievstar.webcameras.CamerasImagesDao;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class CamerasImagesDaoImpl implements CamerasImagesDao {

    private HikariConfig config;
    private HikariDataSource dataSource;

    @PostConstruct
    public void initIt() {
        this.config = new HikariConfig();
        this.config.setJdbcUrl(System.getenv("JDBC_DATABASE_URL"));
        this.dataSource = new HikariDataSource(config);
    }

    public void saveImage(String filePath, int cameraId) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO images VALUES (?, ?, now())");
            statement.setInt(1, cameraId);
            statement.setString(2, filePath);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getLastImagePath(int cameraId) {
        String filePath = null;
        try (Connection connection = dataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement("SELECT path FROM images WHERE id = " +
                    "(SELECT max(id) FROM images WHERE camera_id = ?)");
            statement.setInt(1, cameraId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                filePath = resultSet.getString("path");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filePath;
    }

}
