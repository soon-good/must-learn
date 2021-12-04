# [MacOS] jenv 설치

jenv 설치 방법을 정리했는줄 알았는데 정리하지 않았었다니... 이직준비하던 시절이라, 깃헙을 어뷰징하는 것처럼 보일까봐 양심상 찔린다고 생각했던건지 에버노트에만 남아있었다.<br>

일단 이것도 기록으로 남겨놔야 개발환경 설치할 때 삽질을 안한다.<br>

## jenv 설치

```bash
$ brew install jenv
```

<br>

### adoptopenjdk 설치

그냥 바이너리를 홈페이지에서 다운로드 받아 설치하는 것이 좋다. 요즘 brew 리포지터리에 올라가는 adoptopenjdk 가 설치에 실패할때가 간간히 있었던것 같다. 이 외에도 실제 개발환경으로 사용하는 라이브러리는 공식 홈페이지에서 제공하는 바이너리를 다운받는 것이 권장되는 편이다.<br>

<br>

## jenv 에 jdk 추가

```bash
$ ls -al /Library/Java/JavaVirtualMachines
$ jenv add /Library/Java/JavaVirtualMachines/adoptopenjdk-16.jdk/Contents/Home

$ jenv versions
  system
  1.8
  1.8.0.292
  16
  16.0
  16.0.1
  openjdk64-1.8.0.292
* openjdk64-16.0.1

$ jenv global openjdk64-16.0.1
```

