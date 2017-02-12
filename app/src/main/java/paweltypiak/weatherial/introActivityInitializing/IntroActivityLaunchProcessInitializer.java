/*******************************************************************************
 * Copyright 2017 Paweł Typiak
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package paweltypiak.weatherial.introActivityInitializing;

import android.app.Activity;
import paweltypiak.weatherial.utils.SharedPreferencesModifier;
import paweltypiak.weatherial.utils.UsefulFunctions;

class IntroActivityLaunchProcessInitializer {

    IntroActivityLaunchProcessInitializer(Activity activity,
                                                 boolean isFirstLaunch,
                                                 IntroActivityFragmentInsertionInitializer fragmentInsertionInitializer,
                                                 IntroActivityNextButtonInitializer introActivityNextButtonInitializer){
        if(isFirstLaunch){
            initializeFirstLaunch(activity,fragmentInsertionInitializer,introActivityNextButtonInitializer);
        }
        else{
            initializeNextLaunch(activity,fragmentInsertionInitializer,introActivityNextButtonInitializer);
        }
    }

    private void initializeFirstLaunch(Activity activity,
                                       IntroActivityFragmentInsertionInitializer fragmentInsertionInitializer,
                                       IntroActivityNextButtonInitializer introActivityNextButtonInitializer){
        initializeStartFragment(activity,fragmentInsertionInitializer);
        setStartButton(activity,true,introActivityNextButtonInitializer);
    }

    private void initializeStartFragment(Activity activity,
                                         IntroActivityFragmentInsertionInitializer fragmentInsertionInitializer){
        fragmentInsertionInitializer.insertStartFragment(activity);
    }

    private void initializeNextLaunch(Activity activity,
                                      IntroActivityFragmentInsertionInitializer fragmentInsertionInitializer,
                                      IntroActivityNextButtonInitializer introActivityNextButtonInitializer){
        //every next launch of application
        setLanguageVersion(activity);
        initializeConfigurationFragment(activity,fragmentInsertionInitializer);
        setStartButton(activity,false,introActivityNextButtonInitializer);
    }

    private void setLanguageVersion(Activity activity){
        //set saved language version
        int languageVersion=SharedPreferencesModifier.getLanguageVersion(activity);
        if(languageVersion!=-1) UsefulFunctions.setLocale(activity,languageVersion);
    }

    private void initializeConfigurationFragment(Activity activity,
                                                 IntroActivityFragmentInsertionInitializer fragmentInsertionInitializer){
        fragmentInsertionInitializer.insertConfigurationFragment(activity);
    }

    private void setStartButton(Activity activity,
                                boolean isFirstLaunch,
                                IntroActivityNextButtonInitializer introActivityNextButtonInitializer){
        introActivityNextButtonInitializer.setStartButton(activity,isFirstLaunch);
    }
}
