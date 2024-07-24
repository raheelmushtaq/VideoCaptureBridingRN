/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 */

import React from 'react';
import {
  Button,
  SafeAreaView,
  ScrollView,
  StatusBar,
  StyleSheet,
  Text,
  useColorScheme,
  View,
} from 'react-native';

import VidepCaptureModule from './src/videocapture/VideoCaptureModule'

function App(): React.JSX.Element {

  const backgroundStyle = {
    flex:1,
    backgroundColor: 'white',
  };

  const onCaptureButtonPressed=()=>{
    VidepCaptureModule.captureVideo()

  }
  return (
    <SafeAreaView style={backgroundStyle}>
      <StatusBar
        barStyle={ 'light-content'}
        backgroundColor={backgroundStyle.backgroundColor}
      />
  
        <View
          style={[{flex:1},{
            alignItems:'center',
            justifyContent:'center',
          }]}>
          <Button title='Capture Video' onPress={onCaptureButtonPressed}></Button>
        </View>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  sectionContainer: {
    marginTop: 32,
    paddingHorizontal: 24,
  },
  sectionTitle: {
    fontSize: 24,
    fontWeight: '600',
  },
  sectionDescription: {
    marginTop: 8,
    fontSize: 18,
    fontWeight: '400',
  },
  highlight: {
    fontWeight: '700',
  },
});

export default App;
