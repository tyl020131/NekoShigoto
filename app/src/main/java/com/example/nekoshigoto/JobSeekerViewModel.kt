package com.example.nekoshigoto

import androidx.lifecycle.ViewModel

class JobSeekerViewModel : ViewModel(){
    private var jobSeeker = JobSeeker("","","","","","","","","","","" ,0, "", "")
    private var qualification = Qualification("","","","")

    fun getJobSeeker():JobSeeker{
        return this.jobSeeker
    }

    fun setJobSeeker(jobSeeker: JobSeeker) {
        this.jobSeeker = jobSeeker
    }

    fun getQualification():Qualification{
        return this.qualification
    }

    fun setQualification(qualification: Qualification) {
        this.qualification = qualification
    }
}