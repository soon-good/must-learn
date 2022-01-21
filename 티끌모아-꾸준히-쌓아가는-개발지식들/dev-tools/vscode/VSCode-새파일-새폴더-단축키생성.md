# VSCode - 새파일, 새폴더 단축키 지정

WebStorm을 써도 되긴 하지만, 이미 인텔리제이를 헤비하게 쓰고 있어서 WebStorm 까지 쓰면 컴퓨터가 못버티기에 VSCode 사용법을 익히기로 결정. VSCode 의 느낌은 뭔가 우분투같은 느낌이다. <br>

## 참고자료

- [VS Code 새파일, 새폴더 단축키 지정](https://honeyinfo7.tistory.com/m/310)

<br>

## 단축키 만들기

Cmd + Shift + P (커맨드 팔레트) -> Open Keyboard Shortcut (JSON) 검색 후 클릭<br>

이후 나타나는 json 에 아래와 같이 적어준다. 원하는 단축키로 매핑하면됨<br>

```json
// Place your key bindings in this file to override the defaults
[
    {
        "key": "cmd+shift+n",
        "command": "explorer.newFile",
        "when": "!editorFocus"
    },
    {
        "key": "cmd+shift+enter",
        "command": "explorer.newFolder",
        "when": "!editorFocus"
    }
]
```

<br>

## 단축키 사용

- Cmd + Shift + e 를 눌러서 탐색창으로 포커스가 이동하게끔 해준다.
- 새폴더를 만들려고 한다면 위에서 작성한 매핑 기준으로 `Cmd + Shift + Enter` 을 눌러준다.
- 새파일을 만들려고 한다면 위에서 작성한 매핑 기준으로 `Cmd + Shift + n` 을 눌러준다.

<br>

