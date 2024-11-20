package com.samuel.email.signature.generator.service;

import com.samuel.email.signature.generator.models.Company;
import com.samuel.email.signature.generator.models.User;
import org.springframework.stereotype.Service;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.xhtmlrenderer.swing.Java2DRenderer;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;
import java.nio.file.*;

@Service
public class EmailSignatureService {

    public String generateHtmlSignature(User user, Company company) {
        String username = user.getUsername() != null ? user.getUsername() : "N/A";
        String userTitle = user.getUserTitle() != null ? user.getUserTitle() : null;
        String PhoneNumber = user.getPhoneNumber() != null ? user.getPhoneNumber() : null;
        String companyName = company != null ? company.getName() : "IST Africa";
        String companyAddress = company != null ? company.getAddress() : "Kigali • KG 28 Ave, 57 • Rwanda";
        String website = company != null ? company.getWebsite() : "www.ist.com";
        String missionStatement = company != null ? company.getMissionStatement()
                : "Empowering learning, every day and everywhere.";

        String titleClass = (userTitle == null) ? "hide" : "";
        String phoneClass = (PhoneNumber == null) ? "hide" : "";

        return """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8"/>
                    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
                    <style>
                        body {
                            font-family: Arial, sans-serif, "Noto Color Emoji";
                            line-height: 1.5;
                            margin: 0;
                            padding: 0;
                        }
                        .signature-container {
                            max-width: 400px;
                            padding: 10px;

                        }
                        .name {
                            font-size: 34px;
                            font-weight: bold;
                        }
                        .hide {
                            display: none;
                        }
                        .title {
                            font-size: 24px;
                            font-style: italic;
                            margin-bottom: 10px;
                        }
                        .contact-info {
                            margin-bottom: 10px;
                        }
                        .contact-info span {
                            display: inline-block;
                            margin-right: 5px;
                            font-size: 22px;
                        }
                        .company {
                            font-size: 24px;
                            font-weight: bold;
                            margin-bottom: 5px;
                        }
                        .address {
                            font-size: 19px;
                            color: #555;
                            margin-bottom: 5px;
                        }
                        .website {
                            font-size: 19px;
                            color: #007BFF;
                            text-decoration: none;
                        }
                        .quote {
                            font-size: 19px;
                            font-style: italic;
                            color: #555;
                        }
                    </style>
                </head>
                <body>
                    <div class="signature-container">
                        <div class="name">%s</div>
                        <div class="title %s">%s</div>
                        <div class="contact-info %s">
                            <span>📞 M:</span>
                            <span>%s</span>
                        </div>
                        <div class="company">%s</div>
                        <div class="address">%s</div>
                        <a href="%s" class="website">%s</a>
                        <div class="quote">
                           %s
                        </div>
                    </div>
                </body>
                </html>
                """.formatted(
                username,
                titleClass, userTitle != null ? userTitle : "",
                phoneClass, PhoneNumber != null ? PhoneNumber : "",
                companyName,
                companyAddress,
                website,
                website,
                missionStatement);

    }

    public File generateImageFromHtml(String htmlContent, String userEmail,
            String imageDirectory) throws IOException {
        File directory = new File(imageDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String sanitizedEmail = userEmail.replaceAll("[^a-zA-Z0-9.-]", "_");
        String outputPath = imageDirectory + File.separator + sanitizedEmail +
                "_signature.png";

        File tempHtmlFile = File.createTempFile("signature", ".html");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempHtmlFile))) {
            writer.write(htmlContent);
        }

        try {
            Java2DRenderer renderer = new Java2DRenderer(tempHtmlFile, 360);
            BufferedImage image = renderer.getImage();

            File outputFile = new File(outputPath);
            ImageIO.write(image, "png", outputFile);

            return outputFile;
        } finally {
            tempHtmlFile.delete();
        }
    }

}
