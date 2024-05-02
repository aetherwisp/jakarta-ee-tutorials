# Jakarta EE 10 Tutorials
## １．開発環境
### １－１．ソフトウェア
|ソフトウェア|バージョン|説明|
|---|---|---|
|[Adoptium OpenJDK](https://adoptium.net/)|[17.0.11+9](https://adoptium.net/temurin/archive/?version=17)||
|[Eclipse IDE for Enterprise Java and Web Developers](https://www.eclipse.org/downloads/packages/release/2023-12/r/eclipse-ide-enterprise-java-and-web-developers)|[2023-12](https://www.eclipse.org/downloads/download.php?file=/technology/epp/downloads/release/2023-12/R/eclipse-jee-2023-12-R-win32-x86_64.zip)||
|[WildFly](https://www.wildfly.org/)|[31.0.1.Final](https://github.com/wildfly/wildfly/releases/download/31.0.1.Final/wildfly-31.0.1.Final.zip)|WildFly Maven Plugin で使用|
|[Apache Maven](https://maven.apache.org/)|[3.9.6](https://dlcdn.apache.org/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.zip)||

**※ Eclipse を日本語化する場合**<br>
&emsp;[Pleiades 日本語化プラグイン](https://willbrains.jp/#pleiades.html)のサイトから Pleiades プラグイン単体をダウンロードします。<br>
&emsp;ダウンロードした zip を任意のディレクトリに展開し、zip 内の readme/readme_pleiades.txt の内容に従ってインストールした Eclipse を日本語化してください。

### １－２．環境変数
|環境変数|説明|
|---|---|
|JAVA_HOME|Adoptium OpenJDK 17.0.11+9 インストールディレクトリのパスを指定|
|MAVEN_HOME|Apache Maven 3.9.6 インストールディレクトリのパスを指定|
|Path|%Java_HOME%\bin、%MAVEN_HOME%\bin を追加|

&emsp;※ MAVEN_HOME は Maven 3.5 より不要となっていますが、説明を簡単にするために追加しています。

<div style="page-break-before:always"></div>

## ２．チュートリアル
### ２－１．RESTful Web Service
&emsp;この項では Jakarta EE を使用して [RESTful Web サービス](https://jakarta.ee/specifications/restful-ws/)を作成する方法を説明します。<br>
&emsp;作成するサービスは `http://127.0.0.1:8080/<プロジェクト名>/restfulservice/hello` で HTTP GET リクエストを受け入れ、次のような JSON ペイロードを応答するものとします。
```
{ "name": "world" }
```

&emsp;作成するプロジェクトの最終的な構成は以下のようになります。（パッケージは任意）<br>
```
.
│  pom.xml
│
└─src
    ├─main
    │  ├─java
    │  │  └─path
    │  │      └─to
    │  │          └─tutorial
    │  │              └─restfulwebservice
    │  │                      HelloApplication.java
    │  │                      HelloRecord.java
    │  │                      HelloResource.java
    │  │
    │  ├─resources
    │  └─webapp
    └─test
        ├─java
        └─resources
```


#### アプリケーションクラス
&emsp;サービス名は `Application` クラスを継承したサブクラスで次のように宣言します。ここでは `restfulservice` という名前を指定しています。<br>
&emsp;配備するモジュールに `Application` のサブクラスが存在する場合、モジュール内のリソースクラスが検索され、Web リソースとして公開されます。
```
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("restfulservice")
public class HelloApplication extends Application {
}
```

<div style="page-break-before:always"></div>

#### リソースクラス
&emsp;Jakarta EE アプリケーションでは、クライアントとの対話のターゲットを識別するリソースを公開するためのリソースクラスを作成します。`HelloResource.java` は以下のようになります。<br>
```
package path.to.sample;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("hello")
public class HelloResource {

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public HelloRecord hello(@QueryParam("name") String _name) {
        if (null == _name || _name.trim().isEmpty()) {
            _name = "world";
        }

        return new HelloRecord(_name);
    }
}
```
&emsp;提供するリソースとサービスは、グローバルアドレス空間を提供する URI によって識別されます。<br>

&emsp;`@Path` アノテーションは、ユーザが指定した URL とリクエストの処理を担当する Java クラスとの間の接続を確立します。<br>

&emsp;`@GET` アノテーションは、Jakarta REST によって定義された実行時アノテーションの一つであり、同様の名前の HTTP メソッドに対応し、上記のコードではユーザがリソースにアクセスするには `HTTP GET` メソッドが必要であることを示します。Jakarta REST では、一般的な HTTP メソッドである GET、POST、PUT、DELETE、及び HEAD の一連のリクエストメソッド指定子が用意されています。<br>

&emsp;`@Produces` アノテーションは、HTTP リクエストまたはレスポンスの MIME メディアタイプを指定することができる。上記のコードでは、JSON 形式の応答を返すための `application/json` を指定します。<br>
&emsp;戻り値の `HelloRecord` オブジェクトを JSON にシリアライズしてレスポンスが生成されます。<br>

&emsp;`@QueryParam` アノテーションは、リクエスト URI からクエリパラメータを抽出して引数に指定することができます。<br>

<div style="page-break-before:always"></div>

#### レコードクラス
&emsp;`hello(String)` メソッドは `HelloRecord` を返すように定義されています。`record` は Java 16 で追加された新しいレコードクラスです。
```
public record HelloRecord(String _name) {
}
```

&emsp;レコードクラスを従来の POJO にする場合は以下のようになります。
```
public final class HelloRecord {
    private final String name;

    public HelloRecord(String _name_) {
        this.name = _name_;
    }

    public String name() {
        return this.name;
    }
}
```

<div style="page-break-before:always"></div>

#### プロジェクトの設定
&emsp;Maven を使用して CLI からプロジェクトを実行する方法を説明します。<br>
&emsp;このチュートリアルでは `WildFly` を使用しますが、Jakarta EE と互換性のある他のランタイムは[こちらのサイト](https://jakarta.ee/compatibility/download/)で確認できます。<br>
&emsp;実行のためには、`pom.xml` ファイルに依存関係とプラグインを追加する必要があります。
```
・・・
    <dependencies>
        <dependency>
            <groupId>jakarta.platform</groupId>
            <artifactId>jakarta.jakartaee-web-api</artifactId>
            <version>10.0.0</version>
            <scope>provided</scope>
        </dependency>
    </dependencies>
・・・
    <build>
        <finalName>${project.artifactId}</finalName>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.13.0</version>
            </plugin>
            <plugin>
                <artifactId>maven-war-plugin</artifactId>
                <version>3.4.0</version>
                <configuration>
                    <failOnMissingWebXml>false</failOnMissingWebXml>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.wildfly.plugins</groupId>
                <artifactId>wildfly-maven-plugin</artifactId>
                <version>4.2.2.Final</version>
                <configuration>
                    <version>31.0.1.Final</version>
                    <server-config>standalone.xml</server-config>
                </configuration>
            </plugin>
        </plugins>
    </build>
・・・
```
&emsp;[wildfly-maven-plugin](https://docs.wildfly.org/wildfly-maven-plugin/releases/4.2/index.html) は、Jakarta EE アプリケーションのデプロイ、再デプロイ、アンデプロイ、または実行に使用されます。<br>

<div style="page-break-before:always"></div>

#### プロジェクトの実行
&emsp;WildFly のローカルインスタンスを実行する方法はいくつかありますが、通常の実行は [Run Examples](https://docs.wildfly.org/wildfly-maven-plugin/releases/4.2/run-example.html)を、開発時の実行は [Dev Examples](https://docs.wildfly.org/wildfly-maven-plugin/releases/4.2/dev-example.html) を参照してください。<br>
&emsp;このチュートリアルでは下記の Maven ゴールを使用します。
```
mvn clean package wildfly:run
```
&emsp;上記の Maven ゴールはアプリをビルドし、Wildfly に配置します。Wildfly がインストールされていない場合は、`version` で指定したバージョンの Wildfly を自動的にダウンロードして実行し、war ファイルがデプロイされます。`version` を省略した場合は最新の安定板がダウンロードされます。

<div style="page-break-before:always"></div>

#### 動作確認
&emsp;WildFly が正常に起動した場合、サービスが実行されているので下記の URL にアクセスするとレスポンスが返されます。
```
http://127.0.0.1:8080/restful-web-service/restfulservice/hello
または
http://127.0.0.1:8080/restful-web-service/restfulservice/hello?name=XYZ
```
&emsp;コマンドラインから以下のように確認することもできます。
```
curl -v http://127.0.0.1:8080/restful-web-service/restfulservice/hello
*   Trying 127.0.0.1:8080...
* Connected to 127.0.0.1 (127.0.0.1) port 8080
> GET /restful-web-service/restfulservice/hello HTTP/1.1
> Host: 127.0.0.1:8080
> User-Agent: curl/8.4.0
> Accept: */*
>
< HTTP/1.1 200 OK
< Connection: keep-alive
< Content-Type: application/json
< Content-Length: 16
< Date: Thu, 02 May 2024 06:53:53 GMT
<
{"name":"world"}* Connection #0 to host 127.0.0.1 left intact
```
&emsp;URL の構造は以下のようになっています。
```
http://<hostname>:<port>/<context-root>/<REST-config>/<resource-config>
```
&emsp;URL の各パターンは下記の通りです。
|URL パターン|説明|
|---|---|
|hostname|WildFly が実行されているサーバのホスト名または IP アドレス|
|port|WildFly の HTTP 受信ポート。デフォルトは 8080|
|context-root|アプリケーションに割り当てられるコンテキストルート。デフォルトは WAR ファイル名（拡張子除く）|
|REST-config|`@ApplicationPath` アノテーションに指定した値。未指定の場合は単に省略される|
|resource-config|`@Path` アノテーションに指定した値|



<!--
#### プロジェクト作成
&emsp;[Eclipse Starter for Jakarta EE](https://start.jakarta.ee/) を使用して新しいプロジェクトを作成する。<br>
&emsp;Starter ページにアクセスし、次の手順を実行する。<br>
&emsp;１．プロジェクトのオプションを選択する。選択するオプションは以下の通り。
|オプション|設定|
|---|---|
|Jakarta EE version|Jakarta EE 10|
|Jakarta EE profile|Web Profile|
|Java SE version|Java SE 17|
|Runtime|WildFly|
|Docker support|No|

&emsp;２．Generate ボタンをクリックし、プロジェクト構造とサンプルコードを含む zip ファイルをダウンロードする。
-->