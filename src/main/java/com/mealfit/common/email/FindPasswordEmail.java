package com.mealfit.common.email;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public class FindPasswordEmail implements SendingEmailStrategy {

    private final String url;
    private final String username;
    private final String temporaryPassword;

    public FindPasswordEmail(String url, String username, String temporaryPassword) {
        this.url = url;
        this.username = username;
        this.temporaryPassword = temporaryPassword;
    }

    @Override
    public void fillMessage(MimeMessage message) {
        try {
            String charSet = "utf-8";
            message.setSubject("비밀번호 찾기 - MealFit", charSet);

            message.setText(new StringBuffer()
                  .append("<!DOCTYPE html>\n")
                  .append("<html lang=\"en\" style=\"margin: 0; font-family: 'GmarketSans'; box-sizing: border-box; text-decoration: none; color: inherit;\">\n")
                  .append("<head>\n")
                  .append("  <meta charset=\"UTF-8\">\n")
                  .append("  <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n")
                  .append("  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n")
                  .append("  <style>\n")
                  .append("    @font-face {\n")
                  .append("      font-family: 'GmarketSans';\n")
                  .append("      src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2001@1.1/GmarketSansMedium.woff') format('woff');\n")
                  .append("      font-weight: normal;\n")
                  .append("      font-style: normal;\n")
                  .append("    }\n")
                  .append("  </style>\n")
                  .append("</head>\n")
                  .append("<body style=\"margin: 0; font-family: 'GmarketSans'; box-sizing: border-box; text-decoration: none; color: inherit;\">\n")
                  .append("<div class=\"Wrap\" style=\"font-family: 'GmarketSans'; box-sizing: border-box; text-decoration: none; color: inherit; background-color: #fff; width: 600px; height: 600px; margin-top: 30px; justify-content: center;\">\n")
                  .append("  <div class=\"line\" style=\"margin: 0; font-family: 'GmarketSans'; box-sizing: border-box; text-decoration: none; color: inherit; width: 100%; height: 4px; background-color: #FF7770; margin-bottom: 50px;\"></div>\n")
                  .append("  <header class=\"header\" style=\"margin: 0; font-family: 'GmarketSans'; box-sizing: border-box; text-decoration: none; color: inherit; padding-left: 50px; margin-bottom: 50px;\">\n")
                  .append("    <h1 class=\"Logo\" style=\"margin: 0; font-family: 'GmarketSans'; box-sizing: border-box; text-decoration: none; color: inherit;\">\n")
                  .append("      <img src=\"https://mealfit-bucket.s3.ap-northeast-2.amazonaws.com/mealfit_4.png\" alt=\"Logo\" style=\"margin: 0; font-family: 'GmarketSans'; box-sizing: border-box; text-decoration: none; color: inherit;\">\n")
                  .append("    </h1>\n")
                  .append("    <h2 class=\"Title\" style=\"margin: 0; font-family: 'GmarketSans'; box-sizing: border-box; text-decoration: none; color: inherit; margin-top: 12px; font-size: 26px;\"><span class=\"another\" style=\"margin: 0; font-family: 'GmarketSans'; box-sizing: border-box; text-decoration: none; color: #FF7770;\">임시 비밀번호</span> 안내</h2>\n")
                  .append("  </header>\n")
                  .append("  <main class=\"content\" style=\"margin: 0; font-family: 'GmarketSans'; box-sizing: border-box; text-decoration: none; color: inherit; padding-left: 50px;\">\n")
                  .append("    <p class=\"content-text\" style=\"font-family: 'GmarketSans'; box-sizing: border-box; text-decoration: none; color: inherit; margin: 40px 0; font-size: 18px; line-height: 1.6;\">\n")
                  .append("      안녕하세요.\n")
                  .append("      밀핏(Mealfit)을 이용해 주셔서 감사합니다.\n")
                  .append("      아래 '로그인 하기' 버튼을 클릭하여 로그인을 진행해주세요.\n")
                  .append("      로그인 후, 꼭 비밀번호를 변경해주세요.\n")
                  .append("      로그인을 하지 않을 시, 밀핏(Mealfit)의 일부 서비스가 제한됩니다.\n")
                  .append("      감사합니다.\n")
                  .append("    </p>\n")
                  .append("    <p class=\"info\" style=\"margin: 0; font-family: 'GmarketSans'; box-sizing: border-box; text-decoration: none; color: #fff; width: 20%; border-radius: 6px; margin-right: 15px; height: 100%; background-color: #FF7770; text-align: center; line-height: 50px; display: inline-block;\">비밀번호</p>\n")
                  .append("    <span class=\"username\" style=\"margin: 0; font-family: 'GmarketSans'; box-sizing: border-box; text-decoration: none; color: inherit; height: 100%; width: 70%;\">")
                  .append(temporaryPassword)
                  .append("</span>\n")
                  .append("    <br style=\"margin: 0; font-family: 'GmarketSans'; box-sizing: border-box; text-decoration: none; color: inherit;\">\n")
                  .append("    <button class=\"VerifyBtn\" style=\"margin: 0; font-family: 'GmarketSans'; box-sizing: border-box; text-decoration: none; color: #fff; width: 300px; height: 60px; background-color: #FF7770; border: none; font-size: 18px; margin-top: 40px;\">\n")
                  .append("      <a href=\"")
                  .append(url)
                  .append("\"")
                  .append(" target=\"_blank\" style=\"margin: 0;font-family: 'GmarketSans';box-sizing: border-box;text-decoration: none;color: inherit;width: 100%;height: 100%;display: block;line-height: 60px;\">로그인 하기</a>\n")
                  .append("    </button>\n")
                  .append("  </main>\n")
                  .append("  <div class=\"second-line\" style=\"margin: 0; font-family: 'GmarketSans'; box-sizing: border-box; text-decoration: none; color: inherit; width: 100%; border: 1px solid #ddd; margin-top: 40px; margin-bottom: 14px;\"></div>\n")
                  .append("  <footer class=\"footer\" style=\"margin: 0; font-family: 'GmarketSans'; box-sizing: border-box; text-decoration: none; color: #aaa; padding-left: 50px; font-size: 12px; line-height: 1.5;\">\n")
                  .append("    <div style=\"margin: 0; font-family: 'GmarketSans'; box-sizing: border-box; text-decoration: none; color: inherit;\">\n")
                  .append("      이 메일은 발신 전용 이메일로 회신이 불가능합니다.<br style=\"margin: 0; font-family: 'GmarketSans'; box-sizing: border-box; text-decoration: none; color: inherit;\">\n")
                  .append("      ⓒ 항해99 - 동북 이노베이션 1조 밀핏(Mealfit)\n")
                  .append("    </div>\n")
                  .append("  </footer>\n")
                  .append("</div>\n")
                  .append("</body>\n")
                  .append("</html>")
                  .toString(), charSet, "html");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
