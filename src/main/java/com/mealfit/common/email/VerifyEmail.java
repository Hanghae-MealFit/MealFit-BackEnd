package com.mealfit.common.email;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public class VerifyEmail implements SendingEmailStrategy {

    private final String url;
    private final String username;
    private final String authKey;

    public VerifyEmail(String url, String username, String authKey) {
        this.url = url;
        this.username = username;
        this.authKey = authKey;
    }

    @Override
    public void fillMessage(MimeMessage message) {
        try {
            String charSet = "utf-8";
            message.setSubject("회원가입 이메일 인증 - MealFit", charSet);

            message.setText(new StringBuffer()
                  .append("<!DOCTYPE html>\n")
                  .append("<html lang=\"en\" style=\"margin: 0; font-family: 'GmarketSans'; box-sizing: border-box; text-decoration: none; color: inherit;\">\n")
                  .append("<head>\n")
                  .append("    <meta charset=\"UTF-8\">\n")
                  .append("    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n")
                  .append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n")
                  .append("    <style>\n")
                  .append("        @font-face {\n")
                  .append("            font-family: 'GmarketSans';\n")
                  .append("            src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2001@1.1/GmarketSansMedium.woff') format('woff');\n")
                  .append("            font-weight: normal;\n")
                  .append("            font-style: normal;\n")
                  .append("        }\n")
                  .append("    </style>\n")
                  .append("</head>\n")
                  .append("<body style=\"margin: 0; font-family: 'GmarketSans'; box-sizing: border-box; text-decoration: none; color: inherit;\">\n")
                  .append("<div class=\"Wrap\" style=\"font-family: 'GmarketSans'; box-sizing: border-box; text-decoration: none; color: inherit; background-color: #fff; width: 600px; height: 600px; margin-top: 30px; justify-content: center;\">\n")
                  .append("    <div class=\"line\" style=\"margin: 0; font-family: 'GmarketSans'; box-sizing: border-box; text-decoration: none; color: inherit; width: 100%; height: 4px; background-color: #FF7770; margin-bottom: 50px;\"></div>\n")
                  .append("    <header class=\"header\" style=\"margin: 0; font-family: 'GmarketSans'; box-sizing: border-box; text-decoration: none; color: inherit; padding-left: 50px; margin-bottom: 50px;\">\n")
                  .append("        <h1 class=\"Logo\" style=\"margin: 0; font-family: 'GmarketSans'; box-sizing: border-box; text-decoration: none; color: inherit;\">\n")
                  .append("            <img src=\"https://mealfit-bucket.s3.ap-northeast-2.amazonaws.com/mealfit_4.png\" alt=\"Logo\" style=\"margin: 0; font-family: 'GmarketSans'; box-sizing: border-box; text-decoration: none; color: inherit;\">\n")
                  .append("        </h1>\n")
                  .append("        <h2 class=\"Title\" style=\"margin: 0; font-family: 'GmarketSans'; box-sizing: border-box; text-decoration: none; color: inherit; margin-top: 12px; font-size: 26px;\">회원가입 <span class=\"another\" style=\"margin: 0; font-family: 'GmarketSans'; box-sizing: border-box; text-decoration: none; color: #FF7770;\">이메일 인증</span> 안내</h2>\n")
                  .append("    </header>\n")
                  .append("    <main class=\"content\" style=\"margin: 0; font-family: 'GmarketSans'; box-sizing: border-box; text-decoration: none; color: inherit; padding-left: 50px;\">\n")
                  .append("        <p class=\"content-text\" style=\"font-family: 'GmarketSans'; box-sizing: border-box; text-decoration: none; color: inherit; margin: 40px 0; font-size: 18px; line-height: 1.6;\">\n")
                  .append("            안녕하세요.<br style=\"margin: 0; font-family: 'GmarketSans'; box-sizing: border-box; text-decoration: none; color: inherit;\">\n")
                  .append("            밀핏(Mealfit)을 이용해 주셔서 감사합니다.<br style=\"margin: 0; font-family: 'GmarketSans'; box-sizing: border-box; text-decoration: none; color: inherit;\">\n")
                  .append("            아래 <span class=\"another\" style=\"margin: 0; font-family: 'GmarketSans'; box-sizing: border-box; text-decoration: none; color: #FF7770;\">'이메일 인증하기'</span> 버튼을 클릭하여 메일 인증을 완료해 주세요.<br style=\"margin: 0; font-family: 'GmarketSans'; box-sizing: border-box; text-decoration: none; color: inherit;\">\n")
                  .append("            메일 인증을 하지 않을 경우, 밀핏(Mealfit) 로그인이 불가능 합니다.<br style=\"margin: 0; font-family: 'GmarketSans'; box-sizing: border-box; text-decoration: none; color: inherit;\">\n")
                  .append("            감사합니다.\n")
                  .append("        </p>\n")
                  .append("        <button class=\"VerifyBtn\" style=\"margin: 0; font-family: 'GmarketSans'; box-sizing: border-box; text-decoration: none; color: #fff; width: 300px; height: 60px; background-color: #FF7770; border: none; font-size: 18px;\">\n")
                  .append("            <a href=\"")
                  .append(url)
                  .append("?username=")
                  .append(username)
                  .append("&code=")
                  .append(authKey)
                        .append("\"")
                  .append(" target=\"_blank\" style=\"margin: 0;font-family: 'GmarketSans';box-sizing: border-box;text-decoration: none;color: inherit;width: 100%;height: 100%;display: block;line-height: 60px;\">이메일 인증하기</a>\n")
                  .append("        </button>\n")
                  .append("    </main>\n")
                  .append("    <div class=\"second-line\" style=\"margin: 0; font-family: 'GmarketSans'; box-sizing: border-box; text-decoration: none; color: inherit; width: 100%; border: 1px solid #ddd; margin-top: 40px; margin-bottom: 14px;\"></div>\n")
                  .append("    <footer class=\"footer\" style=\"margin: 0; font-family: 'GmarketSans'; box-sizing: border-box; text-decoration: none; color: #aaa; padding-left: 50px; font-size: 12px; line-height: 1.5;\">\n")
                  .append("        <div style=\"margin: 0; font-family: 'GmarketSans'; box-sizing: border-box; text-decoration: none; color: inherit;\">\n")
                  .append("            이 메일은 발신 전용 이메일로 회신이 불가능합니다.<br style=\"margin: 0; font-family: 'GmarketSans'; box-sizing: border-box; text-decoration: none; color: inherit;\">\n")
                  .append("            ⓒ 항해99 - 동북 이노베이션 1조 밀핏(Mealfit)\n")
                  .append("        </div>\n")
                  .append("    </footer>\n")
                  .append("</div>\n")
                  .append("</body>\n")
                  .append("</html>")
                  .toString(), charSet, "html");
        } catch (MessagingException e) {
            throw new IllegalArgumentException("잘못된 전송 형식입니다. 이메일 전송에 실패했습니다.");
        }
    }
}
