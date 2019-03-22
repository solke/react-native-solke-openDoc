
# react-native-solke-open-doc

## Getting started

`$ npm install react-native-solke-open-doc --save`

### Mostly automatic installation

`$ react-native link react-native-solke-open-doc`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-solke-open-doc` and add `RNSolkeOpenDoc.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNSolkeOpenDoc.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.solke.openDoc.RNSolkeOpenDocPackage;` to the imports at the top of the file
  - Add `new RNSolkeOpenDocPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-solke-open-doc'
  	project(':react-native-solke-open-doc').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-solke-open-doc/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-solke-open-doc')
  	```


## Usage
```javascript
import RNSolkeOpenDoc from 'react-native-solke-open-doc';

// TODO: What to do with the module?
RNSolkeOpenDoc;
```
  