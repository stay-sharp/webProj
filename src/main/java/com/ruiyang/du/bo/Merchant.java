package com.ruiyang.du.bo;

public class Merchant {
    private String parentMerchantNo="10015386831";
    private String merchantNo;
    private String privateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCPssk8SXy12oTYyLOA1fQInwGqXlFSCO56b/ALb0n1FOQtyMDYP7oIbJsShf6Q0JTjxyfLhS4SxIyWtkgk9sv8Vvu7nbXHXRbQPUXFKnN/n8rouALPROcagkGe4/2R3sEABw6nKzUp62X8FWneGI5f8qAe3bX/e0NTPa44rJnDfGBJO0hirrlA7OxXDzw8bMwcOf7CEHTMUtBIafqSNMXxLToWUJ9iJ4lJDlCcV4jJ7Ul9O9yBZzvmhGS5L94q1+Rgbav7+BX51nnG9lZGBb7vdNxruPUnq90bUG3Io4PAtgqqaku3rNQzP8d+Z1jyrWoh+zOLngApaV/BB6+v/A/BAgMBAAECggEBAICfMjrROFx6ZPC57NpEJ/PTh6UcWUPJlNRo/37stVE2yuskR2EI4ZaKx98zquGqoaqkZ71mzw+jeIeqTzcu5PVQUnDjwILbkwD/50SNM+ane/MESheGgCmdL+lt/1ki/rPsnTQKm8KS3q1d6W4PotjFDoeyQiaVCXUnv5sg+10ayw8SnqU5X8VdQSnczYmoxD2fdg7c7zCefptC6UcJyBcSvAGpFiu0LE413yUJ7BenvYIKSlQTYBpyDlA1EGe9yLi42AmQSTIg8c8bUEnXnWWrti8k2WVDks9GvA+Lw2f213XVvowOwY3bDaOOFDtrUbRUE5el5UEZl8qhTH4YiVUCgYEA3Zk0yrIu6X+jgZyP+aFWEc34cowPzJK3YC5YIEuNGPoFlb81DatIaSMhisn3aWR/mA2gEkTSqRaX9E+fY8DjNdIHAj/SWlIxmAzXVnaCidsiyRNUEALkv9eknuOJ0MGIxGrDLu3kebmhBJP97IqTU1UpjyN5QbSJ6cQq+lKH6NcCgYEApgGqSdPiCavssz/58NoWRRyQVnmSQUUmyAIUCEX8rZ7lm+p5CPM2P+UeLB2drDjKDaZD0Da726LSAuz8f8CB3+PErLNjJ2E71tIvGLcxKpdfb0DDOekfCoEAT/TTKcQMEiAw/2AVdZPbzJ9/51XrORBXL+V6JiWFmvw5fxv/QScCgYEA1l6KgnejCEFwxEbXxO69W5X8fZtfAVEBUmsi14Me6QWdd4K0aRfEgej5XtEIpg0Rnd+4dVPc0rRHpZGNKKwFYSBfpV5mOPgAgA4UFtGocIHsAcbP8HlMHwYWe5q0zJ0cmJ6LWQ/LkbHwwYQFBdn8sYXWIWF4wQZM0Db0OJq0XZsCgYACob4gY4KO4rHi+Z2tytdViylipZTDAiSmQRouM2XZHs7HFQWMNcbjZm9/BY0tYM0bQEqM74E07zTjJPyvzc6BTSweDM4CntEDC9wBSU43PaUer2ko7uA2G2t02Q3L+La0RtxgABb3ATwX12OAuGT9R5wKP1obZrbiiSMkN75pOwKBgArkzsjmZWUuOTUjLayGn7HskEyE3FzYm0JrDM/DAzDZwX/twYnVJaoYWsvm53ypYIu/dfs0+P36pXIqY08Nn3mq8TWbUjJXrIGTElHgAlnl0CGz7yg4l43wynH8rZUinyOkN1hnZp8b02zvovvppqSd9nsZ3SFF+4k0mHESdTPa";
    private String hmacKey;

    public String getParentMerchantNo() {
        return parentMerchantNo;
    }

    public void setParentMerchantNo(String parentMerchantNo) {
        this.parentMerchantNo = parentMerchantNo;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getHmacKey() {
        return hmacKey;
    }

    public void setHmacKey(String hmacKey) {
        this.hmacKey = hmacKey;
    }
}
