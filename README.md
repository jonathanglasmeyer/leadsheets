# leadsheets
this appis in a very early stage right now. the basic idea is to display and create music lead sheets based on *.txt-files,
which are locally saved on your sd-card, so that you can sync them with external tools like FolderSync to services like Google Drive or 
Dropbox, with the following goals: 

- easier creation of the leadsheets from a pc/notebook
- continuous backup of the data
- no walled garden (like pretty much every leadsheet app that is pdf based)
- in contrast to editing pdfs (drawing on them and stuff), changes to text files are stored instantly in the file itself, whereas annotated pdfs need to be 
exported manually - this makes it easy to loose your changes if your device crashes and you didn't manually export the changes.
- it's possible to use the data even if you don't want to use the app anymore, since it's just plain text

the following features are on the agenda:
- different folders for different projets
- one easy to use, sortable setlist per folder
- fullscreen display of all sheets beneath each other for live performance (-> less touching the screen during songs)

## screenshots
![Adding songs to setlist](http://i.imgur.com/ktuzSiI.png =400x )
![Performance View](http://i.imgur.com/W6WXVQb.png)
![Sorting](http://i.imgur.com/7c5nkpV.png)

## library credits
Right now I'm making use of the following wonderful libraries:

    compile 'com.android.support:appcompat-v7:21.0.3'
    compile group: 'com.google.guava', name: 'guava', version: '18.0'
    compile 'io.reactivex:rxjava:1.0.0'
    compile 'com.nispok:snackbar:2.8.0'
    compile 'com.jakewharton:butterknife:6.1.0'
    compile 'com.h6ah4i.android.widget.advrecyclerview:advrecyclerview:0.6.1'
    compile 'de.greenrobot:eventbus:2.4.0'

    compile 'com.squareup.dagger:dagger:1.2.2'
    provided 'com.squareup.dagger:dagger-compiler:1.2.2'
    compile 'com.jakewharton.timber:timber:2.7.1'
